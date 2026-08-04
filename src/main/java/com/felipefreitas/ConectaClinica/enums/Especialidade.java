package com.felipefreitas.ConectaClinica.enums;

import lombok.Getter;

@Getter
public enum Especialidade {

    CARDIOLOGIA("Cardiologia"),
    DERMATOLOGIA("Dermatologia"),
    ENDOCRINOLOGIA("Endocrinologia e Metabologia"),
    GINECOLOGIA_OBSTETRICIA("Ginecologia e Obstetrícia"),
    NEUROLOGIA("Neurologia"),
    OFTALMOLOGIA("Oftalmologia"),
    ORTOPEDIA_TRAUMATOLOGIA("Ortopedia e Traumatologia"),
    OTORRINOLARINGOLOGIA("Otorrinolaringologia"),
    PEDIATRIA("Pediatria"),
    PSIQUIATRIA("Psiquiatria"),
    CLINICA_GERAL("Clínica Geral / Medicina Interna"),
    UROLOGIA("Urologia"),
    GASTROENTEROLOGIA("Gastroenterologia"),
    PNEUMOLOGIA("Pneumologia"),
    GERIATRIA("Geriatria");

    private final String descricao;

    Especialidade(String descricao) {
        this.descricao = descricao;
    }
}