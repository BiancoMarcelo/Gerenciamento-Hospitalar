package com.clinica_service.clinica_service.controller;


import com.clinica_service.clinica_service.dto.consultadto.ConsultaDTO;
import com.clinica_service.clinica_service.dto.consultadto.atenderconsulta.AtenderConsultaRequestDTO;
import com.clinica_service.clinica_service.dto.consultadto.verificarconsulta.VerificarConsultaRequestDTO;
import com.clinica_service.clinica_service.dto.consultadto.verificarconsulta.VerificarConsultaResponseDTO;
import com.clinica_service.clinica_service.model.Consulta;
import com.clinica_service.clinica_service.model.TipoConsulta;
import com.clinica_service.clinica_service.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Consulta", description = "Endpoints para criação e gerenciamento de consultas")
@Slf4j
@RestController
@RequestMapping("/api/clinica")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;


    @Operation(summary = "Verificar disponibilidade de consulta",
            description = "Verifica se o horário solicitado esta disponível para agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horário disponível para agendamento"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Horário não disponível para agendamento")
    })
    @PostMapping("/verificar-disponibilidade")
    public ResponseEntity<VerificarConsultaResponseDTO> verificarDisponibilidadeDeConsulta(
            @Valid @RequestBody VerificarConsultaRequestDTO requestDTO) {
        log.info("Verificando disponibilidade para consulta de especilidade: {}, no horário: {}", requestDTO.getEspecialidade(), requestDTO.getHorario());

        VerificarConsultaResponseDTO responseDTO = consultaService.verificarDisponibilidadeMedico(requestDTO);

        if (!responseDTO.isDisponivel()) {
            return ResponseEntity.status(409).body(responseDTO);
        }

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Verificar Consultas por CPF",
            description = "Retorna lista de consultas agendadas por CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de agendamentos do usuário"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/consultas/{cpf}")
    public ResponseEntity<List<ConsultaDTO>> listarPorCpf(@PathVariable String cpf) {
        log.info("Listando consultas do CPF: {}", cpf);
        List<ConsultaDTO> consultas = consultaService.listarPorCPF(cpf);
        return ResponseEntity.ok(consultas);
    }

    @Operation(summary = "Verificar Consultas Agendadas",
            description = "Retorna lista de todas as consultas com Status de 'AGENDADAS'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de agendamentos"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/consultas/agendadas")
    public ResponseEntity<List<Consulta>> listarAgendadas() {
        log.info("Listando todas consultas agendadas");
        List<Consulta> consultas = consultaService.listarTodasConsultasAgendadas();
        return ResponseEntity.ok(consultas);
    }

    @Operation(summary = "Encerrar Consulta",
            description = "Encerra a consulta a partir de um CPF e ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Encerra a consulta"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/consultas/{cpf}/{id}")
    public ResponseEntity<ConsultaDTO> encerrarConsulta(
            @PathVariable String cpf,
            @PathVariable Long id) {
        log.info("Recebendo requisição para encerrar consulta");
        return ResponseEntity.ok(consultaService.atualizarConsulta(cpf, id));
    }

    @Operation(summary = "Listar Consultas",
            description = "Retorna todas as Consultas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todas as consultas"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/consultas")
    public ResponseEntity<List<ConsultaDTO>> listarConsultas() {
        return ResponseEntity.ok(consultaService.listarTodasConsultas());
    }

    @Operation(summary = "Deleta todas as Consultas",
            description = "Retorna todas as Consultas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleta todas as consultas"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @DeleteMapping("/consultas")
    public ResponseEntity<Void> deletarConsultas(){
        consultaService.deletarConsultas();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validar Consulta",
            description = "Valida se uma consulta existe pelo nome e seu horário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleta todas as consultas"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/validar-consulta")
    public ResponseEntity<String> validarConsulta(
            @RequestParam String nomeConsulta,
            @RequestParam LocalDateTime horario) {
        log.info("Informando o nome da consulta: {} ", nomeConsulta);
        consultaService.validarHorarioEConsulta(nomeConsulta, horario);
        return ResponseEntity.ok().build();
    }


}
