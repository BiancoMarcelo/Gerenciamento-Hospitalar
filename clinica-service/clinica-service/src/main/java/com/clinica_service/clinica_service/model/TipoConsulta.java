package com.clinica_service.clinica_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoConsulta {

    CLINICO_GERAL("Clínico Geral", 20),
    PEDIATRIA("Pediatra", 30),
    CARDIOLOGIA("Cardiologista", 40),
    GINECOLOGIA("Ginecologista", 30),
    ORTOPEDIA("Ortopedista", 20),
    DERMATOLOGIA("Dermatologista", 20),
    OFTALMOLOGIA("Oftalmologista", 30),
    PSIQUIATRIA("Psiquiatra", 60),
    OTORRINOLARINGOLOGIA("Otorrinolaringologista", 30),
    UROLOGIA("Urologista", 20);

    private final String descricao;
    private final Integer duracaoConsulta;

    public static TipoConsulta fromDescricao(String descricao) {
        for (TipoConsulta tipoConsulta : values()) {
            if (tipoConsulta.descricao.equalsIgnoreCase(descricao.trim())) {
                return tipoConsulta;
            }
        }
        throw new IllegalArgumentException("Tipo de consulta solicitada não atendida pela rede: " + descricao);
    }
}
