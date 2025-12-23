package com.clinica_service.clinica_service.controller;


import com.clinica_service.clinica_service.dto.medicodto.MedicoDTO;
import com.clinica_service.clinica_service.service.MedicoService;
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

@Tag(name = "Medico", description = "Endpoints para criação e gerenciamento de medicos")
@Slf4j
@RestController
@RequestMapping("/api/clinica/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @Operation(summary = "Criar Médico",
            description = "Cria um novo médico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<MedicoDTO> criar(@Valid @RequestBody MedicoDTO medicoDTO) {
        log.info("Cadastrando novo médico");
        MedicoDTO medicoCriado = medicoService.criarNovoMedico(medicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoCriado);
    }

    @Operation(summary = "Buscar Médico por Id",
            description = "Busca um médico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o médico pelo seu Id"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando médico por ID: {}", id);
        MedicoDTO medico = medicoService.buscarPorId(id);
        return ResponseEntity.ok(medico);
    }

    @Operation(summary = "Listar todos os Médicos",
            description = "Retorna uma lista com todos os médicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista com todos os médicos"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarTodos() {
        log.info("Listando todos os médicos");
        List<MedicoDTO> medicos = medicoService.listarTodosOsMedicos();
        return ResponseEntity.ok(medicos);
    }

    @Operation(summary = "Atualizar médico",
            description = "Atualiza um Médico pelo seu Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicoDTO medicoDTO) {

        log.info("Atualizando médico ID: {}", id);
        MedicoDTO medicoAtualizado = medicoService.atualizar(id, medicoDTO);
        return ResponseEntity.ok(medicoAtualizado);
    }

    @Operation(summary = "Deletar médico",
            description = "Deleta um Médico pelo seu Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médico deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Deletando médico ID: {}", id);
        medicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
