package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.ConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRespository extends JpaRepository<ConsultaEntity, Long> {
}
