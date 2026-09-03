package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FuncionarioRepository extends JpaRepository<FuncionarioEntity, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
