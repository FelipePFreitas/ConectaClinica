package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.EspecialidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EspecialidadeRepository extends JpaRepository<EspecialidadeEntity, Long> {

    Optional<EspecialidadeEntity> findByNomeEspecialidadeIgnoreCase(String nomeEspecialidade);
}
