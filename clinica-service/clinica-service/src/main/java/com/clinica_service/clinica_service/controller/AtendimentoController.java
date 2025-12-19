package com.clinica_service.clinica_service.controller;

import com.clinica_service.clinica_service.dto.consultadto.atenderconsulta.AtenderConsultaRequestDTO;
import com.clinica_service.clinica_service.dto.consultadto.atenderconsulta.AtenderConsultaResponseDTO;
import com.clinica_service.clinica_service.model.Atendimento;
import com.clinica_service.clinica_service.service.AtendimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/clinica")
@RequiredArgsConstructor
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    @PostMapping("/atender-consulta")
    public ResponseEntity<AtenderConsultaResponseDTO> atenderConsulta (
            @Valid @RequestBody AtenderConsultaRequestDTO requestDTO) {
        log.info("Recebendo requisição para atender consulta - CPF: {}", requestDTO.getCpfPaciente());

        AtenderConsultaResponseDTO responseDTO = atendimentoService.atenderConsulta(requestDTO);

        return ResponseEntity.ok(responseDTO);
    }
}
