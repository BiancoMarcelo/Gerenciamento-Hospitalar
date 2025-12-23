package com.medicina_service.medicina_service.controller;

import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoRequestDTO;
import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoResponseDTO;
import com.medicina_service.medicina_service.dto.modeldto.ExameDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoResponseDTO;
import com.medicina_service.medicina_service.model.Exame;
import com.medicina_service.medicina_service.model.Procedimento;
import com.medicina_service.medicina_service.model.TipoProcedimento;
import com.medicina_service.medicina_service.service.ExameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Exames", description = "Endpoints para criação e gerenciamento de Exames")
@Slf4j
@RestController
@RequestMapping("/api/exames")
@RequiredArgsConstructor
public class ExameController {

    private final ExameService exameService;

    @Operation(summary = "Criar Exame",
            description = "Cria um novo exame")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exame criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<ProcedimentoResponseDTO> criarExame (
            @Valid @RequestBody ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Criando exame para CPF: {}", procedimentoRequestDTO.getCpfPaciente());

        ProcedimentoResponseDTO procedimentoResponseDTO = exameService.criarExame(procedimentoRequestDTO);

        return ResponseEntity.ok(procedimentoResponseDTO);

    }

    @Operation(summary = "Agendar Horario",
            description = "Agenda um horário para um exame criado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping("/agendar-horario")
    public ResponseEntity<ConfirmacaoDeCriacaoResponseDTO> marcarHorario (
            @Valid @RequestBody ConfirmacaoDeCriacaoRequestDTO confirmacaoDeCriacaoRequestDTO) {
        log.info("Agendando horário para exame de Id: {}", confirmacaoDeCriacaoRequestDTO.getProcedimentoId());

        ConfirmacaoDeCriacaoResponseDTO confirmacaoDeCriacaoResponseDTO = exameService
                .marcarHorario(confirmacaoDeCriacaoRequestDTO);

        return ResponseEntity.ok(confirmacaoDeCriacaoResponseDTO);

    }

    @Operation(summary = "Listar Exames por CPF",
            description = "Retorna a lista de exames de um CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/{cpf}")
    public ResponseEntity<List<ExameDTO>> listarExamesPorCpf (
            @PathVariable String cpf) {
        log.info("Buscando agendamentos para o CPF: {}", cpf);
        List<ExameDTO> exames = exameService.listarPorCpf(cpf);

        return ResponseEntity.ok(exames);
    }

    @Operation(summary = "Listar Exames",
            description = "Lista todos os exames")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de lista com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping
    public ResponseEntity<List<ExameDTO>> listarTodosExames() {
        log.info("Listando todos os exames");
        List<ExameDTO> exames = exameService.listarExames();
        return ResponseEntity.ok(exames);
    }

    @Operation(summary = "Exames Aguardando Agendamento",
            description = "Retorna uma lista de exames aguardando agendamento de horário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/aguardando-agendamento")
    public ResponseEntity<List<ExameDTO>> listarExamesAguardandoAgendamento() {
        log.info("Listando todos os exames aguardando agendamento");
        List<ExameDTO> exames = exameService.listarAguardandoAgendamento();
        return ResponseEntity.ok(exames);
    }

    @Operation(summary = "Encerrar Exame",
            description = "Encerra um exame a partir do ID do exame")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exame encerrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/encerrar/{id}")
    public ResponseEntity<Void> encerrarExame (
            @PathVariable Long id,
            @Valid @RequestBody ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Encerrando procedimento de id: {}", id);
        exameService.encerrarExame(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validar Exame",
            description = "Verifica se o exame escolhido é atendido pela clínica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno de exame disponível"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/validar-exame/{nomeExame}")
    public ResponseEntity<String> validarExame(@PathVariable String nomeExame) {
        TipoProcedimento exameEncontrado = exameService.validarProcedimento(nomeExame);
        return ResponseEntity.ok("Exame válido: " + exameEncontrado.name());
    }
}
