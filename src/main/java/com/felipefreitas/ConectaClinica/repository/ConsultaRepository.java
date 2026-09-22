package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.ConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ConsultaRepository extends JpaRepository<ConsultaEntity, UUID> {

    List<ConsultaEntity> findByPacienteIdOrderByInicioDesc(UUID pacienteId);

    List<ConsultaEntity> findByProfissionalIdOrderByInicioDesc(UUID profissionalId);

    @Query(value = """
            select exists(
                select 1
                from consultas c
                where c.paciente_id = :pacienteId
                  and c.status in (:statuses)
                  and c.inicio < :fim
                  and (c.inicio + (c.duracao_minutos * interval '1 minute')) > :inicio
            )
            """, nativeQuery = true)
    boolean existsConflitoPaciente(@Param("pacienteId") UUID pacienteId,
                                   @Param("inicio") OffsetDateTime inicio,
                                   @Param("fim") OffsetDateTime fim,
                                   @Param("statuses") Collection<String> statuses);

    @Query(value = """
            select exists(
                select 1
                from consultas c
                where c.paciente_id = :pacienteId
                  and c.status in (:statuses)
                  and c.id <> :consultaIgnoradaId
                  and c.inicio < :fim
                  and (c.inicio + (c.duracao_minutos * interval '1 minute')) > :inicio
            )
            """, nativeQuery = true)
    boolean existsConflitoPacienteIgnorandoConsulta(@Param("pacienteId") UUID pacienteId,
                                                    @Param("inicio") OffsetDateTime inicio,
                                                    @Param("fim") OffsetDateTime fim,
                                                    @Param("statuses") Collection<String> statuses,
                                                    @Param("consultaIgnoradaId") UUID consultaIgnoradaId);

    @Query(value = """
            select exists(
                select 1
                from consultas c
                where c.profissional_id = :profissionalId
                  and c.status in (:statuses)
                  and c.inicio < :fim
                  and (c.inicio + (c.duracao_minutos * interval '1 minute')) > :inicio
            )
            """, nativeQuery = true)
    boolean existsConflitoProfissional(@Param("profissionalId") UUID profissionalId,
                                       @Param("inicio") OffsetDateTime inicio,
                                       @Param("fim") OffsetDateTime fim,
                                       @Param("statuses") Collection<String> statuses);

    @Query(value = """
            select exists(
                select 1
                from consultas c
                where c.profissional_id = :profissionalId
                  and c.status in (:statuses)
                  and c.id <> :consultaIgnoradaId
                  and c.inicio < :fim
                  and (c.inicio + (c.duracao_minutos * interval '1 minute')) > :inicio
            )
            """, nativeQuery = true)
    boolean existsConflitoProfissionalIgnorandoConsulta(@Param("profissionalId") UUID profissionalId,
                                                        @Param("inicio") OffsetDateTime inicio,
                                                        @Param("fim") OffsetDateTime fim,
                                                        @Param("statuses") Collection<String> statuses,
                                                        @Param("consultaIgnoradaId") UUID consultaIgnoradaId);
}
