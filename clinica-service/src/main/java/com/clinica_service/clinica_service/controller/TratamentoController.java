package com.clinica_service.clinica_service.controller;

import com.clinica_service.clinica_service.dto.doencadto.DoencaDTO;
import com.clinica_service.clinica_service.dto.tratamentodto.TratamentoDTO;
import com.clinica_service.clinica_service.service.TratamentoService;
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

@Tag(name = "Tratamento", description = "Endpoints para criação e gerenciamento de tratamentos")
@Slf4j
@RestController
@RequestMapping("/api/clinica/tratamentos")
@RequiredArgsConstructor
public class TratamentoController {

    private final TratamentoService tratamentoService;

    @Operation(summary = "Listar todos os tratamentos",
            description = "Retorna uma lista com todos os tratamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamentos listados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping
    public ResponseEntity<List<TratamentoDTO>> listarTodosTratamentos () {
        log.info("Listando todas os tratamentos");
        List<TratamentoDTO> tratamentos = tratamentoService.listarTratamentos();
        return ResponseEntity.ok(tratamentos);
    }

    @Operation(summary = "Buscar um tratamento pelo Id",
            description = "Retorna um tratamento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamento encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/{id}")
    public  ResponseEntity<TratamentoDTO> listarTratamentoPorId(@PathVariable Long id) {
        log.info("Buscando tratamento de id: {} ", id);
        return ResponseEntity.ok(tratamentoService.buscarTratamento(id));
    }

    @Operation(summary = "Cadastrar tratamentos",
            description = "Cadastra um novo tratamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamento cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<TratamentoDTO> cadastrarNovoTratamento(@Valid @RequestBody TratamentoDTO tratamentoDTO) {
        log.info("Cadastrando novo tratamento: {} ", tratamentoDTO.getDescricaoTratamento());
        return ResponseEntity.status(HttpStatus.CREATED).body(tratamentoService.criarTratamento(tratamentoDTO));
    }

    @Operation(summary = "Atualizar tratamentos",
            description = "Atualiza um tratamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TratamentoDTO> atualizarTratamento(@PathVariable Long id, @Valid @RequestBody TratamentoDTO tratamentoDTO) {
        log.info("Atualizado doença de id: {} ", id);
        return ResponseEntity.ok(tratamentoService.atualizarTratamento(id, tratamentoDTO));
    }

    @Operation(summary = "Deletar tratamentos",
            description = "Deleta um tratamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tratamento deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTratamento(@PathVariable Long id) {
        log.info("Deletando tratamento de id: {} ", id);
        tratamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
