package com.medicina_service.medicina_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoProcedimento {

    COLETA_SANGUE("Coleta de Sangue", false, 15),
    ELETROCARDIOGRAMA("Eletrocardiograma", false, 15),
    RESSONANCIA("Ressonância Magnética", true, 60),
    TOMOGRAFIA("Tomografia", true, 30),
    RAIO_X_CONTRASTADO("Raio-X com Contraste", true, 40),
    CIRURGIA_GERAL("Cirurgia Geral", true, 60),
    BIOPSIA("Biópsia", true, 50),
    ENDOSCOPIA("Endoscopia", true, 60);

    private final String descricao;
    private final boolean altaComplexidade;
    private final Integer duracaoProcedimento;


    public static TipoProcedimento fromDescricao(String descricao) {
        for (TipoProcedimento tipo : values()) {
            if (tipo.descricao.equalsIgnoreCase(descricao.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de procedimento não atendido pela rede: " + descricao);
    }

}
