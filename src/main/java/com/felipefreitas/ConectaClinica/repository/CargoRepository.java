package com.felipefreitas.ConectaClinica.repository;

import com.felipefreitas.ConectaClinica.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<CargoEntity,Long> {

    Optional<CargoEntity> findByNomeCargoIgnoreCase(String nomeCargo);

}
