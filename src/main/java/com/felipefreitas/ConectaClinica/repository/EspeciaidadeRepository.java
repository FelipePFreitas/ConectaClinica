package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.EspecialidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspeciaidadeRepository extends JpaRepository<EspecialidadeEntity, Long> {
}
