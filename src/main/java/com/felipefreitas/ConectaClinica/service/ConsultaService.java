package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.agendamento.BloqueioAgendaCriacaoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.BloqueioAgendaResponseDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaCancelamentoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaCriacaoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaReagendamentoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaResponseDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaStatusRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.PacienteResumoDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ProfissionalResumoDTO;
import com.felipefreitas.ConectaClinica.entity.BloqueioAgendaEntity;
import com.felipefreitas.ConectaClinica.entity.ConsultaEntity;
import com.felipefreitas.ConectaClinica.entity.HistoricoConsultaEntity;
import com.felipefreitas.ConectaClinica.entity.PacienteEntity;
import com.felipefreitas.ConectaClinica.entity.ProfissionalEntity;
import com.felipefreitas.ConectaClinica.enums.AcaoHistoricoConsulta;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.enums.StatusConsulta;
import com.felipefreitas.ConectaClinica.enums.TipoCancelamentoConsulta;
import com.felipefreitas.ConectaClinica.exceptions.BaseExceptions;
import com.felipefreitas.ConectaClinica.repository.BloqueioAgendaRepository;
import com.felipefreitas.ConectaClinica.repository.ConsultaRepository;
import com.felipefreitas.ConectaClinica.repository.HistoricoConsultaRepository;
import com.felipefreitas.ConectaClinica.repository.PacienteRepository;
import com.felipefreitas.ConectaClinica.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private static final Set<StatusConsulta> STATUS_ATIVOS = EnumSet.allOf(StatusConsulta.class)
            .stream()
            .filter(StatusConsulta::ocupaAgenda)
            .collect(java.util.stream.Collectors.toUnmodifiableSet());

    private static final Map<StatusConsulta, Set<StatusConsulta>> TRANSICOES_PERMITIDAS = criarTransicoesPermitidas();

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final BloqueioAgendaRepository bloqueioAgendaRepository;
    private final HistoricoConsultaRepository historicoConsultaRepository;
    private final AgendamentoAutorizacaoService autorizacaoService;

    @Transactional
    public ConsultaResponseDTO agendar(ConsultaCriacaoRequestDTO request, Authentication authentication) {
        String autor = autorizacaoService.exigirGestaoAgenda(authentication);
        validarPeriodo(request.inicio(), request.inicio().plusMinutes(request.duracaoMinutos()));

        PacienteEntity paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new BaseExceptions(ErrorEnum.PACIENTE_NAO_ENCONTRADO));
        ProfissionalEntity profissional = profissionalRepository.findById(request.profissionalId())
                .orElseThrow(() -> new BaseExceptions(ErrorEnum.PROFISSIONAL_NAO_ENCONTRADO));
        validarProfissionalAtivo(profissional);
        validarConflitos(paciente.getId(), profissional.getId(), request.inicio(),
                request.inicio().plusMinutes(request.duracaoMinutos()), null);

        OffsetDateTime agora = OffsetDateTime.now();
        var consulta = ConsultaEntity.builder()
                .paciente(paciente)
                .profissional(profissional)
                .inicio(request.inicio())
                .duracaoMinutos(request.duracaoMinutos())
                .modalidade(request.modalidade())
                .localOuCanal(request.localOuCanal().trim())
                .status(StatusConsulta.AGENDADA)
                .criadoPor(autor)
                .criadoEm(agora)
                .atualizadoPor(autor)
                .atualizadoEm(agora)
                .build();

        ConsultaEntity consultaSalva = consultaRepository.save(consulta);
        registrarHistorico(consultaSalva, AcaoHistoricoConsulta.CRIACAO, null,
                consultaSalva.getStatus(), autor, "Consulta agendada");

        return toResponse(consultaSalva);
    }

    @Transactional(readOnly = true)
    public ConsultaResponseDTO buscar(UUID id, Authentication authentication) {
        autorizacaoService.exigirUsuarioAutenticado(authentication);
        return toResponse(buscarConsulta(id));
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorPaciente(UUID pacienteId, Authentication authentication) {
        autorizacaoService.exigirUsuarioAutenticado(authentication);
        return consultaRepository.findByPacienteIdOrderByInicioDesc(pacienteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorProfissional(UUID profissionalId, Authentication authentication) {
        autorizacaoService.exigirUsuarioAutenticado(authentication);
        return consultaRepository.findByProfissionalIdOrderByInicioDesc(profissionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ConsultaResponseDTO alterarStatus(UUID id, ConsultaStatusRequestDTO request, Authentication authentication) {
        String autor = autorizacaoService.exigirGestaoAgenda(authentication);
        ConsultaEntity consulta = buscarConsulta(id);

        if (request.status() == StatusConsulta.CANCELADA_PELO_PACIENTE
                || request.status() == StatusConsulta.CANCELADA_PELA_CLINICA) {
            TipoCancelamentoConsulta tipo = request.status() == StatusConsulta.CANCELADA_PELO_PACIENTE
                    ? TipoCancelamentoConsulta.PACIENTE
                    : TipoCancelamentoConsulta.CLINICA;
            return cancelar(consulta, tipo, request.motivo(), autor);
        }

        validarConsultaAlteravel(consulta);
        if (!TRANSICOES_PERMITIDAS.getOrDefault(consulta.getStatus(), Set.of()).contains(request.status())) {
            throw new BaseExceptions(ErrorEnum.TRANSICAO_STATUS_INVALIDA);
        }
        validarRegistroDeFalta(consulta, request.status());

        StatusConsulta statusAnterior = consulta.getStatus();
        consulta.setStatus(request.status());
        auditarAtualizacao(consulta, autor);
        ConsultaEntity consultaSalva = consultaRepository.save(consulta);
        registrarHistorico(consultaSalva, AcaoHistoricoConsulta.ALTERACAO_STATUS, statusAnterior,
                request.status(), autor, request.motivo());

        return toResponse(consultaSalva);
    }

    @Transactional
    public ConsultaResponseDTO cancelar(UUID id, ConsultaCancelamentoRequestDTO request, Authentication authentication) {
        String autor = autorizacaoService.exigirGestaoAgenda(authentication);
        return cancelar(buscarConsulta(id), request.tipo(), request.motivo(), autor);
    }

    @Transactional
    public ConsultaResponseDTO reagendar(UUID id, ConsultaReagendamentoRequestDTO request, Authentication authentication) {
        String autor = autorizacaoService.exigirGestaoAgenda(authentication);
        ConsultaEntity consulta = buscarConsulta(id);
        validarConsultaAlteravel(consulta);

        Integer novaDuracao = request.duracaoMinutos() == null ? consulta.getDuracaoMinutos() : request.duracaoMinutos();
        OffsetDateTime novoFim = request.inicio().plusMinutes(novaDuracao);
        validarPeriodo(request.inicio(), novoFim);
        validarConflitos(consulta.getPaciente().getId(), consulta.getProfissional().getId(),
                request.inicio(), novoFim, consulta.getId());

        StatusConsulta statusAnterior = consulta.getStatus();
        String detalhe = "De " + consulta.getInicio() + " até " + consulta.getFim()
                + " para " + request.inicio() + " até " + novoFim
                + (request.motivo() == null || request.motivo().isBlank() ? "" : ". Motivo: " + request.motivo());

        consulta.setInicio(request.inicio());
        consulta.setDuracaoMinutos(novaDuracao);
        consulta.setStatus(StatusConsulta.AGENDADA);
        auditarAtualizacao(consulta, autor);

        ConsultaEntity consultaSalva = consultaRepository.save(consulta);
        registrarHistorico(consultaSalva, AcaoHistoricoConsulta.REAGENDAMENTO, statusAnterior,
                consultaSalva.getStatus(), autor, detalhe);

        return toResponse(consultaSalva);
    }

    @Transactional
    public BloqueioAgendaResponseDTO bloquearAgenda(BloqueioAgendaCriacaoRequestDTO request, Authentication authentication) {
        String autor = autorizacaoService.exigirGestaoAgenda(authentication);
        validarPeriodo(request.inicio(), request.fim());

        ProfissionalEntity profissional = profissionalRepository.findById(request.profissionalId())
                .orElseThrow(() -> new BaseExceptions(ErrorEnum.PROFISSIONAL_NAO_ENCONTRADO));
        validarProfissionalAtivo(profissional);

        if (consultaRepository.existsConflitoProfissional(profissional.getId(), request.inicio(),
                request.fim(), statusAtivosComoString())
                || bloqueioAgendaRepository.existsConflitoProfissional(profissional.getId(), request.inicio(), request.fim())) {
            throw new BaseExceptions(ErrorEnum.CONFLITO_AGENDA_PROFISSIONAL);
        }

        var bloqueio = BloqueioAgendaEntity.builder()
                .profissional(profissional)
                .inicio(request.inicio())
                .fim(request.fim())
                .motivo(request.motivo().trim())
                .criadoPor(autor)
                .criadoEm(OffsetDateTime.now())
                .build();

        return toResponse(bloqueioAgendaRepository.save(bloqueio));
    }

    @Transactional(readOnly = true)
    public List<BloqueioAgendaResponseDTO> listarBloqueios(UUID profissionalId, Authentication authentication) {
        autorizacaoService.exigirUsuarioAutenticado(authentication);
        return bloqueioAgendaRepository.findByProfissionalIdOrderByInicioDesc(profissionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ConsultaResponseDTO cancelar(ConsultaEntity consulta, TipoCancelamentoConsulta tipo, String motivo, String autor) {
        validarConsultaAlteravel(consulta);
        if (motivo == null || motivo.isBlank()) {
            throw new BaseExceptions(ErrorEnum.VALOR_INVALIDO);
        }

        StatusConsulta statusAnterior = consulta.getStatus();
        StatusConsulta statusPosterior = tipo == TipoCancelamentoConsulta.PACIENTE
                ? StatusConsulta.CANCELADA_PELO_PACIENTE
                : StatusConsulta.CANCELADA_PELA_CLINICA;

        consulta.setStatus(statusPosterior);
        consulta.setCanceladoPor(autor);
        consulta.setCanceladoEm(OffsetDateTime.now());
        consulta.setMotivoCancelamento(motivo.trim());
        auditarAtualizacao(consulta, autor);

        ConsultaEntity consultaSalva = consultaRepository.save(consulta);
        registrarHistorico(consultaSalva, AcaoHistoricoConsulta.CANCELAMENTO, statusAnterior,
                statusPosterior, autor, motivo.trim());

        return toResponse(consultaSalva);
    }

    private ConsultaEntity buscarConsulta(UUID id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new BaseExceptions(ErrorEnum.CONSULTA_NAO_ENCONTRADA));
    }

    private void validarProfissionalAtivo(ProfissionalEntity profissional) {
        if (!profissional.isAtivo()) {
            throw new BaseExceptions(ErrorEnum.PROFISSIONAL_INATIVO);
        }
    }

    private void validarConsultaAlteravel(ConsultaEntity consulta) {
        if (consulta.getStatus().encerrado()) {
            throw new BaseExceptions(ErrorEnum.CONSULTA_ENCERRADA);
        }
    }

    /**
     * A tolerância para caracterização da falta ainda depende de decisão de negócio.
     * Enquanto isso, aplica-se o requisito mínimo explícito: a falta só pode ser
     * registrada após o início previsto e nunca para uma consulta futura.
     */
    private void validarRegistroDeFalta(ConsultaEntity consulta, StatusConsulta novoStatus) {
        if (novoStatus == StatusConsulta.NAO_COMPARECEU
                && !OffsetDateTime.now().isAfter(consulta.getInicio())) {
            throw new BaseExceptions(ErrorEnum.PERIODO_AGENDA_INVALIDO);
        }
    }

    /**
     * A regra de antecedência mínima ainda está pendente de decisão de negócio.
     * A implementação mínima apenas recusa períodos passados, nulos ou invertidos.
     */
    private void validarPeriodo(OffsetDateTime inicio, OffsetDateTime fim) {
        OffsetDateTime agora = OffsetDateTime.now();
        if (inicio == null || fim == null || !fim.isAfter(inicio) || !inicio.isAfter(agora)) {
            throw new BaseExceptions(ErrorEnum.PERIODO_AGENDA_INVALIDO);
        }
    }

    private void validarConflitos(UUID pacienteId, UUID profissionalId, OffsetDateTime inicio,
                                  OffsetDateTime fim, UUID consultaIgnoradaId) {
        boolean conflitoPaciente = consultaIgnoradaId == null
                ? consultaRepository.existsConflitoPaciente(pacienteId, inicio, fim, statusAtivosComoString())
                : consultaRepository.existsConflitoPacienteIgnorandoConsulta(pacienteId, inicio, fim,
                statusAtivosComoString(), consultaIgnoradaId);
        if (conflitoPaciente) {
            throw new BaseExceptions(ErrorEnum.CONFLITO_AGENDA_PACIENTE);
        }

        boolean conflitoProfissional = consultaIgnoradaId == null
                ? consultaRepository.existsConflitoProfissional(profissionalId, inicio, fim, statusAtivosComoString())
                : consultaRepository.existsConflitoProfissionalIgnorandoConsulta(profissionalId, inicio, fim,
                statusAtivosComoString(), consultaIgnoradaId);
        if (conflitoProfissional
                || bloqueioAgendaRepository.existsConflitoProfissional(profissionalId, inicio, fim)) {
            throw new BaseExceptions(ErrorEnum.CONFLITO_AGENDA_PROFISSIONAL);
        }
    }

    private void auditarAtualizacao(ConsultaEntity consulta, String autor) {
        consulta.setAtualizadoPor(autor);
        consulta.setAtualizadoEm(OffsetDateTime.now());
    }

    private void registrarHistorico(ConsultaEntity consulta, AcaoHistoricoConsulta acao,
                                    StatusConsulta statusAnterior, StatusConsulta statusPosterior,
                                    String autor, String detalhe) {
        var historico = HistoricoConsultaEntity.builder()
                .consulta(consulta)
                .acao(acao)
                .statusAnterior(statusAnterior)
                .statusPosterior(statusPosterior)
                .autor(autor)
                .ocorridoEm(OffsetDateTime.now())
                .detalhe(detalhe)
                .build();
        historicoConsultaRepository.save(historico);
    }

    private List<String> statusAtivosComoString() {
        return STATUS_ATIVOS.stream().map(Enum::name).toList();
    }

    private ConsultaResponseDTO toResponse(ConsultaEntity consulta) {
        PacienteEntity paciente = consulta.getPaciente();
        ProfissionalEntity profissional = consulta.getProfissional();
        return new ConsultaResponseDTO(
                consulta.getId(),
                new PacienteResumoDTO(paciente.getId(), paciente.getNome()),
                new ProfissionalResumoDTO(profissional.getId(), profissional.getNome(), profissional.isAtivo()),
                consulta.getInicio(),
                consulta.getFim(),
                consulta.getDuracaoMinutos(),
                consulta.getModalidade(),
                consulta.getLocalOuCanal(),
                consulta.getStatus(),
                consulta.getCriadoPor(),
                consulta.getCriadoEm(),
                consulta.getAtualizadoPor(),
                consulta.getAtualizadoEm(),
                consulta.getCanceladoPor(),
                consulta.getCanceladoEm(),
                consulta.getMotivoCancelamento()
        );
    }

    private BloqueioAgendaResponseDTO toResponse(BloqueioAgendaEntity bloqueio) {
        ProfissionalEntity profissional = bloqueio.getProfissional();
        return new BloqueioAgendaResponseDTO(
                bloqueio.getId(),
                new ProfissionalResumoDTO(profissional.getId(), profissional.getNome(), profissional.isAtivo()),
                bloqueio.getInicio(),
                bloqueio.getFim(),
                bloqueio.getMotivo(),
                bloqueio.getCriadoPor(),
                bloqueio.getCriadoEm()
        );
    }

    private static Map<StatusConsulta, Set<StatusConsulta>> criarTransicoesPermitidas() {
        var transicoes = new EnumMap<StatusConsulta, Set<StatusConsulta>>(StatusConsulta.class);
        transicoes.put(StatusConsulta.SOLICITADA, EnumSet.of(StatusConsulta.AGENDADA));
        transicoes.put(StatusConsulta.AGENDADA, EnumSet.of(
                StatusConsulta.CONFIRMADA,
                StatusConsulta.CANCELADA_PELO_PACIENTE,
                StatusConsulta.CANCELADA_PELA_CLINICA,
                StatusConsulta.REAGENDAMENTO_SOLICITADO
        ));
        transicoes.put(StatusConsulta.CONFIRMADA, EnumSet.of(
                StatusConsulta.EM_ATENDIMENTO,
                StatusConsulta.CANCELADA_PELO_PACIENTE,
                StatusConsulta.CANCELADA_PELA_CLINICA,
                StatusConsulta.NAO_COMPARECEU
        ));
        transicoes.put(StatusConsulta.EM_ATENDIMENTO, EnumSet.of(StatusConsulta.CONCLUIDA));
        transicoes.put(StatusConsulta.REAGENDAMENTO_SOLICITADO, EnumSet.of(
                StatusConsulta.AGENDADA,
                StatusConsulta.CANCELADA_PELO_PACIENTE,
                StatusConsulta.CANCELADA_PELA_CLINICA
        ));
        return Map.copyOf(transicoes);
    }
}
