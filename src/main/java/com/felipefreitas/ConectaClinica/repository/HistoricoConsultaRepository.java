package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.HistoricoConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HistoricoConsultaRepository extends JpaRepository<HistoricoConsultaEntity, UUID> {
    List<HistoricoConsultaEntity> findByConsultaIdOrderByOcorridoEmDesc(UUID consultaId);
}
