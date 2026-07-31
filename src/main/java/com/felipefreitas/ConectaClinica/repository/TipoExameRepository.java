package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.TipoExameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoExameRepository extends JpaRepository<TipoExameEntity, Long> {
}
