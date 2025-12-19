package com.clinica_service.clinica_service.controller;

import com.clinica_service.clinica_service.dto.doencadto.DoencaDTO;
import com.clinica_service.clinica_service.model.Doenca;
import com.clinica_service.clinica_service.service.AtendimentoService;
import com.clinica_service.clinica_service.service.DoencaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clinica/doencas")
@RequiredArgsConstructor
public class DoencaController {

    private final DoencaService doencaService;

    @GetMapping
    public ResponseEntity<List<DoencaDTO>> listarTodasAsDoencas () {
        log.info("Listando todas as doenças");
        List<DoencaDTO> doencas = doencaService.listarDoencas();
        return ResponseEntity.ok(doencas);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<DoencaDTO> listarDoencaPorId(@PathVariable Long id) {
        log.info("Buscando doença de id: {} ", id);
        return ResponseEntity.ok(doencaService.listarDoencaPorId(id));
    }

    @PostMapping
    public ResponseEntity<DoencaDTO> cadastrarNovaDoenca(@Valid @RequestBody DoencaDTO doencaDTO) {
        log.info("Cadastrando nova doença: {} ", doencaDTO.getNomeDoenca());
        return ResponseEntity.status(HttpStatus.CREATED).body(doencaService.criarDoenca(doencaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoencaDTO> atualizarDoenca(@PathVariable Long id, @Valid @RequestBody DoencaDTO doencaDTO) {
        log.info("Atualizado doença de id: {} ", id);
        return ResponseEntity.ok(doencaService.atualizarDoenca(id, doencaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDoenca(@PathVariable Long id) {
        log.info("Deletando doença de id: {} ", id);
        doencaService.deletarDoenca(id);
        return ResponseEntity.noContent().build();
    }


}
