package com.clinica_service.clinica_service.controller;

import com.clinica_service.clinica_service.dto.doencadto.DoencaDTO;
import com.clinica_service.clinica_service.model.Doenca;
import com.clinica_service.clinica_service.service.AtendimentoService;
import com.clinica_service.clinica_service.service.DoencaService;
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

@Tag(name = "Doença", description = "Endpoints para criação e gerenciamento de doenças")
@Slf4j
@RestController
@RequestMapping("/api/clinica/doencas")
@RequiredArgsConstructor
public class DoencaController {

    private final DoencaService doencaService;

    @Operation(summary = "Listar Doenças",
            description = "Retorna uma lista de todas as doenças")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todas as doenças"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping
    public ResponseEntity<List<DoencaDTO>> listarTodasAsDoencas () {
        log.info("Listando todas as doenças");
        List<DoencaDTO> doencas = doencaService.listarDoencas();
        return ResponseEntity.ok(doencas);
    }

    @Operation(summary = "Busca Doenças por ID",
            description = "Retorna a doença buscada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doença por Id"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/{id}")
    public  ResponseEntity<DoencaDTO> listarDoencaPorId(@PathVariable Long id) {
        log.info("Buscando doença de id: {} ", id);
        return ResponseEntity.ok(doencaService.listarDoencaPorId(id));
    }

    @Operation(summary = "Cadastrar nova doença",
            description = "Cadastra uma nova doença no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doença cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<DoencaDTO> cadastrarNovaDoenca(@Valid @RequestBody DoencaDTO doencaDTO) {
        log.info("Cadastrando nova doença: {} ", doencaDTO.getNomeDoenca());
        return ResponseEntity.status(HttpStatus.CREATED).body(doencaService.criarDoenca(doencaDTO));
    }

    @Operation(summary = "Atualizar doença",
            description = "Atualiza uma doença no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doença atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DoencaDTO> atualizarDoenca(@PathVariable Long id, @Valid @RequestBody DoencaDTO doencaDTO) {
        log.info("Atualizado doença de id: {} ", id);
        return ResponseEntity.ok(doencaService.atualizarDoenca(id, doencaDTO));
    }

    @Operation(summary = "Deletar doença",
            description = "Deleta uma doença no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doença deletada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDoenca(@PathVariable Long id) {
        log.info("Deletando doença de id: {} ", id);
        doencaService.deletarDoenca(id);
        return ResponseEntity.noContent().build();
    }


}
