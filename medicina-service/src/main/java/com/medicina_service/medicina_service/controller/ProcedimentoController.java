package com.medicina_service.medicina_service.controller;

import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoRequestDTO;
import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoResponseDTO;
import com.medicina_service.medicina_service.dto.modeldto.ProcedimentoDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoResponseDTO;
import com.medicina_service.medicina_service.model.Procedimento;
import com.medicina_service.medicina_service.service.ProcedimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Procedimentos", description = "Endpoints para criação e gerenciamento de Procedimentos")
@Slf4j
@RestController
@RequestMapping("/api/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    @Operation(summary = "Criar Procedimento",
            description = "Cria um novo Procedimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procedimento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping("/marcar")
    public ResponseEntity<ProcedimentoResponseDTO> criarProcedimento (
            @Valid @RequestBody ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Criando procedimento para CPF: {}", procedimentoRequestDTO.getCpfPaciente());

        ProcedimentoResponseDTO procedimentoResponseDTO = procedimentoService.criarProcedimento(procedimentoRequestDTO);

        return ResponseEntity.ok(procedimentoResponseDTO);

    }

    @Operation(summary = "Agendar Horario de Procedimento",
            description = "Agenda um horário para um procedimento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping("/agendar-horario")
    public ResponseEntity<ConfirmacaoDeCriacaoResponseDTO> marcarHorario (
            @Valid @RequestBody ConfirmacaoDeCriacaoRequestDTO confirmacaoDeCriacaoRequestDTO) {
        log.info("Agendando horário para procedimento de Id: {}", confirmacaoDeCriacaoRequestDTO.getProcedimentoId());

        ConfirmacaoDeCriacaoResponseDTO confirmacaoDeCriacaoResponseDTO = procedimentoService
                .marcarHorario(confirmacaoDeCriacaoRequestDTO);

        return ResponseEntity.ok(confirmacaoDeCriacaoResponseDTO);

    }

    @Operation(summary = "Listar Procedimentos por CPF",
            description = "Retorna uma lista de todos os procedimentos agendados para um CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/{cpf}")
    public ResponseEntity<List<ProcedimentoDTO>> listarProcedimentosPorCpf (
            @PathVariable String cpf) {
        log.info("Buscando agendamentos para o CPF: {}", cpf);
        List<ProcedimentoDTO> procedimentos = procedimentoService.listarPorCpf(cpf);

        return ResponseEntity.ok(procedimentos);
    }

    @Operation(summary = "Listar Todos Procedimentos",
            description = "Retorna uma lista de todos os procedimentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping
    public ResponseEntity<List<ProcedimentoDTO>> listarTodosProcedimentos() {
        log.info("Listando todos os procedimentos");
        List<ProcedimentoDTO> procedimentos = procedimentoService.listarProcedimentos();
        return ResponseEntity.ok(procedimentos);
    }

    @Operation(summary = "Listar Todos Procedimentos Aguardando Agendamento",
            description = "Retorna uma lista de todos os procedimentos que estão aguardando agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/aguardando-agendamento")
    public ResponseEntity<List<ProcedimentoDTO>> listarProcedimentosAguardandoAgendamento() {
        log.info("Listando todos os procedimentos aguardando agendamento");
        List<ProcedimentoDTO> procedimentos = procedimentoService.listarAguardandoAgendamento();
        return ResponseEntity.ok(procedimentos);
    }

    @Operation(summary = "Encerrar Procedimento",
            description = "Encerra um procedimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procedimento encerrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/encerrar/{id}")
    public ResponseEntity<Void> encerrarProcedimento (
            @PathVariable Long id,
            @Valid @RequestBody ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Encerrando procedimento de id: {}", id);
        procedimentoService.encerrarProcedimento(id);
        return ResponseEntity.noContent().build();
    }

}
