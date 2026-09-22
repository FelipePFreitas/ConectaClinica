package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PacienteRepository extends JpaRepository<PacienteEntity, UUID> {
}
