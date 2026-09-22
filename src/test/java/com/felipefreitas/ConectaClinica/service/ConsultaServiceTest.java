package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.agendamento.BloqueioAgendaCriacaoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaCancelamentoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaCriacaoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaReagendamentoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaStatusRequestDTO;
import com.felipefreitas.ConectaClinica.entity.ConsultaEntity;
import com.felipefreitas.ConectaClinica.entity.PacienteEntity;
import com.felipefreitas.ConectaClinica.entity.ProfissionalEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.enums.ModalidadeConsulta;
import com.felipefreitas.ConectaClinica.enums.StatusConsulta;
import com.felipefreitas.ConectaClinica.enums.TipoCancelamentoConsulta;
import com.felipefreitas.ConectaClinica.exceptions.BaseExceptions;
import com.felipefreitas.ConectaClinica.repository.BloqueioAgendaRepository;
import com.felipefreitas.ConectaClinica.repository.ConsultaRepository;
import com.felipefreitas.ConectaClinica.repository.HistoricoConsultaRepository;
import com.felipefreitas.ConectaClinica.repository.PacienteRepository;
import com.felipefreitas.ConectaClinica.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Agendamento de Consultas - Service")
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private BloqueioAgendaRepository bloqueioAgendaRepository;

    @Mock
    private HistoricoConsultaRepository historicoConsultaRepository;

    private ConsultaService consultaService;

    private final UUID pacienteId = UUID.randomUUID();
    private final UUID profissionalId = UUID.randomUUID();
    private final UUID consultaId = UUID.randomUUID();
    private final OffsetDateTime inicio = OffsetDateTime.now().plusDays(2).withNano(0);
    private final Authentication funcionario = new UsernamePasswordAuthenticationToken(
            "funcionario.teste",
            "senha",
            List.of(new SimpleGrantedAuthority("FUNCIONARIO"))
    );

    @BeforeEach
    void setUp() {
        consultaService = new ConsultaService(
                consultaRepository,
                pacienteRepository,
                profissionalRepository,
                bloqueioAgendaRepository,
                historicoConsultaRepository,
                new AgendamentoAutorizacaoService()
        );
    }

    @Nested
    @DisplayName("Cenários de sucesso")
    class CenariosDeSucesso {

        @Test
        @DisplayName("Deve agendar consulta ativa sem expor entidade")
        void deveAgendarConsultaAtivaSemExporEntidade() {
            // Arrange
            prepararPacienteEProfissionalAtivo();
            when(consultaRepository.save(any(ConsultaEntity.class))).thenAnswer(invocation -> {
                ConsultaEntity consulta = invocation.getArgument(0);
                consulta.setId(consultaId);
                return consulta;
            });

            var request = new ConsultaCriacaoRequestDTO(
                    pacienteId,
                    profissionalId,
                    inicio,
                    30,
                    ModalidadeConsulta.PRESENCIAL,
                    "Sala 1"
            );

            // Act
            var response = consultaService.agendar(request, funcionario);

            // Assert
            assertThat(response.id()).isEqualTo(consultaId);
            assertThat(response.paciente().id()).isEqualTo(pacienteId);
            assertThat(response.profissional().id()).isEqualTo(profissionalId);
            assertThat(response.status()).isEqualTo(StatusConsulta.AGENDADA);
            assertThat(response.fim()).isEqualTo(inicio.plusMinutes(30));
            assertThat(response.criadoPor()).isEqualTo("funcionario.teste");
            verify(historicoConsultaRepository).save(any());
        }

        @Test
        @DisplayName("Deve confirmar consulta agendada quando transição é permitida")
        void deveConfirmarConsultaAgendadaQuandoTransicaoPermitida() {
            // Arrange
            var consulta = consulta(StatusConsulta.AGENDADA);
            when(consultaRepository.findById(consultaId)).thenReturn(Optional.of(consulta));
            when(consultaRepository.save(any(ConsultaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            var response = consultaService.alterarStatus(
                    consultaId,
                    new ConsultaStatusRequestDTO(StatusConsulta.CONFIRMADA, "Paciente confirmou"),
                    funcionario
            );

            // Assert
            assertThat(response.status()).isEqualTo(StatusConsulta.CONFIRMADA);
            assertThat(response.atualizadoPor()).isEqualTo("funcionario.teste");
            verify(historicoConsultaRepository).save(any());
        }

        @Test
        @DisplayName("Deve cancelar consulta e registrar auditoria")
        void deveCancelarConsultaERegistrarAuditoria() {
            // Arrange
            var consulta = consulta(StatusConsulta.CONFIRMADA);
            when(consultaRepository.findById(consultaId)).thenReturn(Optional.of(consulta));
            when(consultaRepository.save(any(ConsultaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            var response = consultaService.cancelar(
                    consultaId,
                    new ConsultaCancelamentoRequestDTO(TipoCancelamentoConsulta.PACIENTE, "Imprevisto familiar"),
                    funcionario
            );

            // Assert
            assertThat(response.status()).isEqualTo(StatusConsulta.CANCELADA_PELO_PACIENTE);
            assertThat(response.canceladoPor()).isEqualTo("funcionario.teste");
            assertThat(response.motivoCancelamento()).isEqualTo("Imprevisto familiar");
            assertThat(response.canceladoEm()).isNotNull();
            verify(historicoConsultaRepository).save(any());
        }

        @Test
        @DisplayName("Deve reagendar consulta preservando histórico")
        void deveReagendarConsultaPreservandoHistorico() {
            // Arrange
            var consulta = consulta(StatusConsulta.CONFIRMADA);
            var novoInicio = inicio.plusDays(1);
            when(consultaRepository.findById(consultaId)).thenReturn(Optional.of(consulta));
            when(consultaRepository.save(any(ConsultaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            var response = consultaService.reagendar(
                    consultaId,
                    new ConsultaReagendamentoRequestDTO(novoInicio, 45, "Paciente pediu novo horário"),
                    funcionario
            );

            // Assert
            assertThat(response.inicio()).isEqualTo(novoInicio);
            assertThat(response.duracaoMinutos()).isEqualTo(45);
            assertThat(response.status()).isEqualTo(StatusConsulta.AGENDADA);
            verify(historicoConsultaRepository).save(any());
        }
    }

    @Nested
    @DisplayName("Falhas de validação e conflito")
    class FalhasDeValidacaoEConflito {

        @Test
        @DisplayName("Deve rejeitar profissional inativo")
        void deveRejeitarProfissionalInativo() {
            // Arrange
            var paciente = PacienteEntity.builder().id(pacienteId).nome("Maria").build();
            var profissional = ProfissionalEntity.builder().id(profissionalId).nome("Dr. João").ativo(false).build();
            when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
            when(profissionalRepository.findById(profissionalId)).thenReturn(Optional.of(profissional));

            var request = new ConsultaCriacaoRequestDTO(
                    pacienteId,
                    profissionalId,
                    inicio,
                    30,
                    ModalidadeConsulta.TELEATENDIMENTO,
                    "Videochamada"
            );

            // Act & Assert
            assertThatThrownBy(() -> consultaService.agendar(request, funcionario))
                    .isInstanceOf(BaseExceptions.class)
                    .extracting(ex -> ((BaseExceptions) ex).getErrorEnum())
                    .isEqualTo(ErrorEnum.PROFISSIONAL_INATIVO);
            verify(consultaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar sobreposição de agenda do paciente")
        void deveRejeitarSobreposicaoAgendaPaciente() {
            // Arrange
            prepararPacienteEProfissionalAtivo();
            when(consultaRepository.existsConflitoPaciente(eq(pacienteId), eq(inicio),
                    eq(inicio.plusMinutes(30)), anyCollection())).thenReturn(true);

            var request = new ConsultaCriacaoRequestDTO(
                    pacienteId,
                    profissionalId,
                    inicio,
                    30,
                    ModalidadeConsulta.PRESENCIAL,
                    "Sala 1"
            );

            // Act & Assert
            assertThatThrownBy(() -> consultaService.agendar(request, funcionario))
                    .isInstanceOf(BaseExceptions.class)
                    .extracting(ex -> ((BaseExceptions) ex).getErrorEnum())
                    .isEqualTo(ErrorEnum.CONFLITO_AGENDA_PACIENTE);
            verify(consultaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar consulta em bloqueio do profissional")
        void deveRejeitarConsultaEmBloqueioProfissional() {
            // Arrange
            prepararPacienteEProfissionalAtivo();
            when(bloqueioAgendaRepository.existsConflitoProfissional(profissionalId, inicio, inicio.plusMinutes(30)))
                    .thenReturn(true);

            var request = new ConsultaCriacaoRequestDTO(
                    pacienteId,
                    profissionalId,
                    inicio,
                    30,
                    ModalidadeConsulta.PRESENCIAL,
                    "Sala 1"
            );

            // Act & Assert
            assertThatThrownBy(() -> consultaService.agendar(request, funcionario))
                    .isInstanceOf(BaseExceptions.class)
                    .extracting(ex -> ((BaseExceptions) ex).getErrorEnum())
                    .isEqualTo(ErrorEnum.CONFLITO_AGENDA_PROFISSIONAL);
            verify(consultaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar transição inválida de confirmada para concluída")
        void deveRejeitarTransicaoInvalidaDeConfirmadaParaConcluida() {
            // Arrange
            when(consultaRepository.findById(consultaId)).thenReturn(Optional.of(consulta(StatusConsulta.CONFIRMADA)));

            // Act & Assert
            assertThatThrownBy(() -> consultaService.alterarStatus(
                    consultaId,
                    new ConsultaStatusRequestDTO(StatusConsulta.CONCLUIDA, "Fim direto não permitido"),
                    funcionario
            ))
                    .isInstanceOf(BaseExceptions.class)
                    .extracting(ex -> ((BaseExceptions) ex).getErrorEnum())
                    .isEqualTo(ErrorEnum.TRANSICAO_STATUS_INVALIDA);
            verify(consultaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar reagendamento de consulta encerrada")
        void deveRejeitarReagendamentoConsultaEncerrada() {
            // Arrange
            when(consultaRepository.findById(consultaId)).thenReturn(Optional.of(consulta(StatusConsulta.CONCLUIDA)));

            // Act & Assert
            assertThatThrownBy(() -> consultaService.reagendar(
                    consultaId,
                    new ConsultaReagendamentoRequestDTO(inicio.plusDays(1), null, "Retorno"),
                    funcionario
            ))
                    .isInstanceOf(BaseExceptions.class)
                    .extracting(ex -> ((BaseExceptions) ex).getErrorEnum())
                    .isEqualTo(ErrorEnum.CONSULTA_ENCERRADA);
            verify(consultaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar gestão de agenda para usuário sem perfil autorizado")
        void deveRejeitarGestaoAgendaParaUsuarioSemPerfilAutorizado() {
            // Arrange
            var paciente = new UsernamePasswordAuthenticationToken(
                    "paciente.teste",
                    "senha",
                    List.of(new SimpleGrantedAuthority("PACIENTE"))
            );
            var request = new ConsultaCriacaoRequestDTO(
                    pacienteId,
                    profissionalId,
                    inicio,
                    30,
                    ModalidadeConsulta.PRESENCIAL,
                    "Sala 1"
            );

            // Act & Assert
            assertThatThrownBy(() -> consultaService.agendar(request, paciente))
                    .isInstanceOf(BaseExceptions.class)
                    .extracting(ex -> ((BaseExceptions) ex).getErrorEnum())
                    .isEqualTo(ErrorEnum.PERMISSAO_AGENDAMENTO_NEGADA);
            verify(pacienteRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Deve rejeitar bloqueio de agenda sobre consulta ativa")
        void deveRejeitarBloqueioAgendaSobreConsultaAtiva() {
            // Arrange
            var profissional = ProfissionalEntity.builder().id(profissionalId).nome("Dra. Ana").ativo(true).build();
            when(profissionalRepository.findById(profissionalId)).thenReturn(Optional.of(profissional));
            when(consultaRepository.existsConflitoProfissional(eq(profissionalId), eq(inicio),
                    eq(inicio.plusMinutes(60)), anyCollection())).thenReturn(true);

            var request = new BloqueioAgendaCriacaoRequestDTO(
                    profissionalId,
                    inicio,
                    inicio.plusMinutes(60),
                    "Reunião clínica"
            );

            // Act & Assert
            assertThatThrownBy(() -> consultaService.bloquearAgenda(request, funcionario))
                    .isInstanceOf(BaseExceptions.class)
                    .extracting(ex -> ((BaseExceptions) ex).getErrorEnum())
                    .isEqualTo(ErrorEnum.CONFLITO_AGENDA_PROFISSIONAL);
            verify(bloqueioAgendaRepository, never()).save(any());
        }
    }

    private void prepararPacienteEProfissionalAtivo() {
        var paciente = PacienteEntity.builder().id(pacienteId).nome("Maria").build();
        var profissional = ProfissionalEntity.builder().id(profissionalId).nome("Dra. Ana").ativo(true).build();
        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
        when(profissionalRepository.findById(profissionalId)).thenReturn(Optional.of(profissional));
    }

    private ConsultaEntity consulta(StatusConsulta status) {
        return ConsultaEntity.builder()
                .id(consultaId)
                .paciente(PacienteEntity.builder().id(pacienteId).nome("Maria").build())
                .profissional(ProfissionalEntity.builder().id(profissionalId).nome("Dra. Ana").ativo(true).build())
                .inicio(inicio)
                .duracaoMinutos(30)
                .modalidade(ModalidadeConsulta.PRESENCIAL)
                .localOuCanal("Sala 1")
                .status(status)
                .criadoPor("funcionario.teste")
                .criadoEm(inicio.minusDays(1))
                .atualizadoPor("funcionario.teste")
                .atualizadoEm(inicio.minusDays(1))
                .build();
    }
}
