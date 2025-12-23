package com.clinica_service.clinica_service.controller;


import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoRequestDTO;
import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoResponseDTO;
import com.clinica_service.clinica_service.model.Procedimento;
import com.clinica_service.clinica_service.service.ProcedimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Procedimento", description = "Endpoints para criação e gerenciamento de procedimentos")
@Slf4j
@RestController
@RequestMapping("/api/clinica/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    @Operation(summary = "Atualizar Dados do Paciente",
            description = "Atualiza os dados de um Paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<ProcedimentoResponseDTO> criar(
            @Valid @RequestBody ProcedimentoRequestDTO request) {

        log.info("Cadastrando novo procedimento");

        Procedimento procedimento = procedimentoService.criar(
                request.getDescricaoProcedimento(),
                request.getAltaComplexidade()
        );

        ProcedimentoResponseDTO response = ProcedimentoResponseDTO.builder()
                .id(procedimento.getId())
                .descricaoProcedimento(procedimento.getDescricaoProcedimento())
                .altaComplexidade(procedimento.getAltaComplexidade())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar procedimento por ID",
            description = "Atualiza os dados de um Paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procedimento encontrado com sucesos"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando procedimento por ID: {}", id);

        Procedimento procedimento = procedimentoService.buscarPorId(id);

        ProcedimentoResponseDTO response = ProcedimentoResponseDTO.builder()
                .id(procedimento.getId())
                .descricaoProcedimento(procedimento.getDescricaoProcedimento())
                .altaComplexidade(procedimento.getAltaComplexidade())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar Procedimentos",
            description = "Retorna um lista de procedimentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procedimento encontrados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping
    public ResponseEntity<List<ProcedimentoResponseDTO>> listarTodos() {
        log.info("Listando todos os procedimentos");

        List<ProcedimentoResponseDTO> procedimentos = procedimentoService.listarTodos()
                .stream()
                .map(p -> ProcedimentoResponseDTO.builder()
                        .id(p.getId())
                        .descricaoProcedimento(p.getDescricaoProcedimento())
                        .altaComplexidade(p.getAltaComplexidade())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(procedimentos);
    }

    @Operation(summary = "Listar Procedimentos de Alta Complexidade",
            description = "Retorna um lista de procedimentos de alta complexidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procedimento encontrados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/alta-complexidade/{altaComplexidade}")
    public ResponseEntity<List<ProcedimentoResponseDTO>> listarPorAltaComplexidade(
            @PathVariable Boolean altaComplexidade) {

        log.info("Listando procedimentos - Alta complexidade: {}", altaComplexidade);

        List<ProcedimentoResponseDTO> procedimentos = procedimentoService
                .listarPorAltaComplexidade(altaComplexidade)
                .stream()
                .map(p -> ProcedimentoResponseDTO.builder()
                        .id(p.getId())
                        .descricaoProcedimento(p.getDescricaoProcedimento())
                        .altaComplexidade(p.getAltaComplexidade())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(procedimentos);
    }

    @Operation(summary = "Atualizar Procedimento",
            description = "Atualiza um procedimento pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procedimento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProcedimentoRequestDTO request) {

        log.info("Atualizando procedimento ID: {}", id);

        Procedimento procedimento = procedimentoService.atualizar(
                id,
                request.getDescricaoProcedimento(),
                request.getAltaComplexidade()
        );

        ProcedimentoResponseDTO response = ProcedimentoResponseDTO.builder()
                .id(procedimento.getId())
                .descricaoProcedimento(procedimento.getDescricaoProcedimento())
                .altaComplexidade(procedimento.getAltaComplexidade())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletar Procedimento",
            description = "Deleta um procedimento pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Procedimento deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Deletando procedimento ID: {}", id);
        procedimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
