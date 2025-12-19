package com.clinica_service.clinica_service.controller;


import com.clinica_service.clinica_service.dto.sintomadto.SintomaDTO;
import com.clinica_service.clinica_service.service.SintomaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clinica/sintomas")
@RequiredArgsConstructor
public class SintomaController {

    private final SintomaService sintomaService;

    @PostMapping
    public ResponseEntity<SintomaDTO> criar(@Valid @RequestBody SintomaDTO sintomaDTO) {
        log.info("Cadastrando novo sintoma");
        SintomaDTO sintomaCriado = sintomaService.criarSintoma(sintomaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(sintomaCriado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SintomaDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando sintoma por ID: {}", id);
        SintomaDTO sintoma = sintomaService.buscarPorId(id);
        return ResponseEntity.ok(sintoma);
    }

    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<SintomaDTO> buscarPorDescricao(@PathVariable String descricao) {
        log.info("Buscando sintoma por descrição: {}", descricao);
        SintomaDTO sintoma = sintomaService.buscarPorDescricao(descricao);
        return ResponseEntity.ok(sintoma);
    }

    @GetMapping
    public ResponseEntity<List<SintomaDTO>> listarTodos() {
        log.info("Listando todos os sintomas");
        List<SintomaDTO> sintomas = sintomaService.listarTodos();
        return ResponseEntity.ok(sintomas);
    }

    @GetMapping("/prioridade/{prioridade}")
    public ResponseEntity<List<SintomaDTO>> listarPorPrioridade(@PathVariable Integer prioridade) {
        log.info("Listando sintomas por prioridade: {}", prioridade);
        List<SintomaDTO> sintomas = sintomaService.listarPorPrioridade(prioridade);
        return ResponseEntity.ok(sintomas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SintomaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody SintomaDTO sintomaDTO) {

        log.info("Atualizando sintoma ID: {}", id);
        SintomaDTO sintomaAtualizado = sintomaService.atualizar(id, sintomaDTO);
        return ResponseEntity.ok(sintomaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Deletando sintoma ID: {}", id);
        sintomaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/tratamentos")
    public ResponseEntity<SintomaDTO> associarTratamentos(
            @PathVariable Long id,
            @RequestBody List<Long> tratamentosIds) {

        log.info("Endpoint: Associando tratamentos ao sintoma ID: {}", id);
        SintomaDTO sintoma = sintomaService.associarTratamentos(id, tratamentosIds);
        return ResponseEntity.ok(sintoma);
    }

}
