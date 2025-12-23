package com.clinica_service.clinica_service.controller;

import com.clinica_service.clinica_service.dto.consultadto.atenderconsulta.AtenderConsultaRequestDTO;
import com.clinica_service.clinica_service.dto.consultadto.atenderconsulta.AtenderConsultaResponseDTO;
import com.clinica_service.clinica_service.model.Atendimento;
import com.clinica_service.clinica_service.service.AtendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Atendimento", description = "Endpoints para criação e gerenciamento de atendimentos de consultas")
@Slf4j
@RestController
@RequestMapping("/api/clinica")
@RequiredArgsConstructor
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    @Operation(summary = "Atender Consulta",
            description = "Iniciar o atendimento de uma consulta agendada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atendimento realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping("/AtenderConsulta")
    public ResponseEntity<AtenderConsultaResponseDTO> atenderConsulta (
            @Valid @RequestBody AtenderConsultaRequestDTO requestDTO) {
        log.info("Recebendo requisição para atender consulta - CPF: {}", requestDTO.getCpfPaciente());

        AtenderConsultaResponseDTO responseDTO = atendimentoService.atenderConsulta(requestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Pronto Atendimento",
            description = "Iniciar o atendimento sem uma consulta agendada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atendimento realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping("/pronto-atendimento")
    public ResponseEntity<AtenderConsultaResponseDTO> totenAtendimento(
            @Valid @RequestBody AtenderConsultaRequestDTO requestDTO) {
        log.info("Recebendo requisição de pronto atendimento para o CPF: {} ", requestDTO.getCpfPaciente());

        AtenderConsultaResponseDTO responseDTO = atendimentoService.prontoAtendimento(requestDTO);

        return ResponseEntity.ok(responseDTO);
    }
}
