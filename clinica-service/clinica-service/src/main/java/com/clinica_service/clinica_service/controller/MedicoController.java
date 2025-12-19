package com.clinica_service.clinica_service.controller;


import com.clinica_service.clinica_service.dto.medicodto.MedicoDTO;
import com.clinica_service.clinica_service.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clinica/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @PostMapping
    public ResponseEntity<MedicoDTO> criar(@Valid @RequestBody MedicoDTO medicoDTO) {
        log.info("Cadastrando novo médico");
        MedicoDTO medicoCriado = medicoService.criarNovoMedico(medicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoCriado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando médico por ID: {}", id);
        MedicoDTO medico = medicoService.buscarPorId(id);
        return ResponseEntity.ok(medico);
    }

    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarTodos() {
        log.info("Listando todos os médicos");
        List<MedicoDTO> medicos = medicoService.listarTodosOsMedicos();
        return ResponseEntity.ok(medicos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicoDTO medicoDTO) {

        log.info("Atualizando médico ID: {}", id);
        MedicoDTO medicoAtualizado = medicoService.atualizar(id, medicoDTO);
        return ResponseEntity.ok(medicoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Deletando médico ID: {}", id);
        medicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
