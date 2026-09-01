package com.felipefreitas.ConectaClinica.mapper;


import com.felipefreitas.ConectaClinica.dto.paciente.PacienteRequestDTO;
import com.felipefreitas.ConectaClinica.dto.paciente.PacienteResponseDTO;
import com.felipefreitas.ConectaClinica.entity.PacienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "cpf", expression = "java(com.felipefreitas.ConectaClinica.util.CPFUtil.cleanCpf(dto.cpf()))")
    @Mapping(target = "email", expression = "java(dto.email().toLowerCase().trim())")

    PacienteEntity toEntity(PacienteRequestDTO dto);

    PacienteResponseDTO toDTO(PacienteEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "email", expression = "java(dto.email().toLowerCase().trim())")

    void updateEntityFromDTO(PacienteRequestDTO dto, @MappingTarget PacienteEntity entity);
}