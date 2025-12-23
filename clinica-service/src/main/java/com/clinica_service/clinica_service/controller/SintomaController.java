package com.clinica_service.clinica_service.controller;


import com.clinica_service.clinica_service.dto.sintomadto.SintomaDTO;
import com.clinica_service.clinica_service.service.SintomaService;
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

@Tag(name = "Sintoma", description = "Endpoints para criação e gerenciamento de sintomas")
@Slf4j
@RestController
@RequestMapping("/api/clinica/sintomas")
@RequiredArgsConstructor
public class SintomaController {

    private final SintomaService sintomaService;

    @Operation(summary = "Criar Sintoma",
            description = "Criar um novo sintoma")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sintoma criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<SintomaDTO> criar(@Valid @RequestBody SintomaDTO sintomaDTO) {
        log.info("Cadastrando novo sintoma");
        SintomaDTO sintomaCriado = sintomaService.criarSintoma(sintomaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(sintomaCriado);
    }

    @Operation(summary = "Buscar Sintoma",
            description = "Busca um sintoma por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sintoma encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SintomaDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando sintoma por ID: {}", id);
        SintomaDTO sintoma = sintomaService.buscarPorId(id);
        return ResponseEntity.ok(sintoma);
    }

    @Operation(summary = "Buscar Sintoma por descrição do sintoma",
            description = "Busca um sintoma pela descrição do sintoma")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sintoma encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<SintomaDTO> buscarPorDescricao(@PathVariable String descricao) {
        log.info("Buscando sintoma por descrição: {}", descricao);
        SintomaDTO sintoma = sintomaService.buscarPorDescricao(descricao);
        return ResponseEntity.ok(sintoma);
    }

    @Operation(summary = "Listar todos os Sintomas",
            description = "Listar todos os sintomas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sintomas encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping
    public ResponseEntity<List<SintomaDTO>> listarTodos() {
        log.info("Listando todos os sintomas");
        List<SintomaDTO> sintomas = sintomaService.listarTodos();
        return ResponseEntity.ok(sintomas);
    }

    @Operation(summary = "Listar Sintomas por Prioridade",
            description = "Retorna uma lista de sintomas pela prioridade desejada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sintomas encontrados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/prioridade/{prioridade}")
    public ResponseEntity<List<SintomaDTO>> listarPorPrioridade(@PathVariable Integer prioridade) {
        log.info("Listando sintomas por prioridade: {}", prioridade);
        List<SintomaDTO> sintomas = sintomaService.listarPorPrioridade(prioridade);
        return ResponseEntity.ok(sintomas);
    }

    @Operation(summary = "Atualizar Sintoma",
            description = "Atualiza um sintoma ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sintoma encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SintomaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody SintomaDTO sintomaDTO) {

        log.info("Atualizando sintoma ID: {}", id);
        SintomaDTO sintomaAtualizado = sintomaService.atualizar(id, sintomaDTO);
        return ResponseEntity.ok(sintomaAtualizado);
    }

    @Operation(summary = "Deletar Sintoma",
            description = "Deleta um sintoma ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sintoma deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Deletando sintoma ID: {}", id);
        sintomaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Associar Tratamentos",
            description = "Associa um tratamento a um sintoma  ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sintoma associado a um tratamento com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/{id}/tratamentos")
    public ResponseEntity<SintomaDTO> associarTratamentos(
            @PathVariable Long id,
            @RequestBody List<Long> tratamentosIds) {

        log.info("Endpoint: Associando tratamentos ao sintoma ID: {}", id);
        SintomaDTO sintoma = sintomaService.associarTratamentos(id, tratamentosIds);
        return ResponseEntity.ok(sintoma);
    }

}
