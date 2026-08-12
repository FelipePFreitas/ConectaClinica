package com.felipefreitas.ConectaClinica.mapper;


import com.felipefreitas.ConectaClinica.dto.paciente.PacienteRequestDTO;
import com.felipefreitas.ConectaClinica.dto.paciente.PacienteResponseDTO;
import com.felipefreitas.ConectaClinica.entity.PacienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "cpf", expression = "java(com.felipefreitas.ConectaClinica.util.CPFUtil.cleanCpf(dto.cpf()))")
    @Mapping(target = "email", expression = "java(dto.email().toLowerCase().trim())")

    PacienteEntity toEntity(PacienteRequestDTO dto);

    PacienteResponseDTO toDTO(PacienteEntity entity);
}