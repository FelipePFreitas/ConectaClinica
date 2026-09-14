package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.RoleUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleUsuarioRepository extends JpaRepository<RoleUsuarioEntity, UUID> {

    Optional<RoleUsuarioEntity> findByNome(String nome);
}
