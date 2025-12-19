package com.clinica_service.clinica_service.controller;


import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoRequestDTO;
import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoResponseDTO;
import com.clinica_service.clinica_service.model.Procedimento;
import com.clinica_service.clinica_service.service.ProcedimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/clinica/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Deletando procedimento ID: {}", id);
        procedimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
