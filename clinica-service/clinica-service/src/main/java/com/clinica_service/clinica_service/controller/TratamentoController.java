package com.clinica_service.clinica_service.controller;

import com.clinica_service.clinica_service.dto.doencadto.DoencaDTO;
import com.clinica_service.clinica_service.dto.tratamentodto.TratamentoDTO;
import com.clinica_service.clinica_service.service.TratamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clinica/tratamentos")
@RequiredArgsConstructor
public class TratamentoController {

    private final TratamentoService tratamentoService;

    @GetMapping
    public ResponseEntity<List<TratamentoDTO>> listarTodosTratamentos () {
        log.info("Listando todas os tratamentos");
        List<TratamentoDTO> tratamentos = tratamentoService.listarTratamentos();
        return ResponseEntity.ok(tratamentos);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<TratamentoDTO> listarTratamentoPorId(@PathVariable Long id) {
        log.info("Buscando tratamento de id: {} ", id);
        return ResponseEntity.ok(tratamentoService.buscarTratamento(id));
    }

    @PostMapping
    public ResponseEntity<TratamentoDTO> cadastrarNovoTratamento(@Valid @RequestBody TratamentoDTO tratamentoDTO) {
        log.info("Cadastrando novo tratamento: {} ", tratamentoDTO.getDescricaoTratamento());
        return ResponseEntity.status(HttpStatus.CREATED).body(tratamentoService.criarTratamento(tratamentoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TratamentoDTO> atualizarTratamento(@PathVariable Long id, @Valid @RequestBody TratamentoDTO tratamentoDTO) {
        log.info("Atualizado doença de id: {} ", id);
        return ResponseEntity.ok(tratamentoService.atualizarTratamento(id, tratamentoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTratamento(@PathVariable Long id) {
        log.info("Deletando tratamento de id: {} ", id);
        tratamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
