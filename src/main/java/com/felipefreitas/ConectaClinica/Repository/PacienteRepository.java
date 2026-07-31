package com.felipefreitas.ConectaClinica.Repository;

import com.felipefreitas.ConectaClinica.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<PacienteEntity, Long> {
}
