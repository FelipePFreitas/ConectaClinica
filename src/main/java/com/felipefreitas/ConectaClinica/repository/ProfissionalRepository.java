package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.ProfissionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfissionalRepository extends JpaRepository<ProfissionalEntity, UUID> {
}
