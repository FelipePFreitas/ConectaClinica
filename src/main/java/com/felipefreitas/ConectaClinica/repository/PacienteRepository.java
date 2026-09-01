package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<PacienteEntity, Long> {

    Optional<PacienteEntity> findByCpf (String cpf);

    boolean existsByCpf (String cpf);

    boolean existsByEmail (String email);

}
