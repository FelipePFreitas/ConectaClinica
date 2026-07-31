package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<MedicoEntity, Long> {
}
