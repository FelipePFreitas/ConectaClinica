package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.BloqueioAgendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface BloqueioAgendaRepository extends JpaRepository<BloqueioAgendaEntity, UUID> {

    List<BloqueioAgendaEntity> findByProfissionalIdOrderByInicioDesc(UUID profissionalId);

    @Query("""
            select (count(b) > 0)
            from BloqueioAgendaEntity b
            where b.profissional.id = :profissionalId
              and b.inicio < :fim
              and b.fim > :inicio
            """)
    boolean existsConflitoProfissional(@Param("profissionalId") UUID profissionalId,
                                       @Param("inicio") OffsetDateTime inicio,
                                       @Param("fim") OffsetDateTime fim);
}
