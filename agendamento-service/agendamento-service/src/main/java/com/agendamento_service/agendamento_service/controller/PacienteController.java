package com.agendamento_service.agendamento_service.controller;

import com.agendamento_service.agendamento_service.dto.pacientedto.PacienteDTO;
import com.agendamento_service.agendamento_service.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.RescaleOp;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDTO>> buscarPacientes() {
        log.info("Buscando todos os pacientes");
        List<PacienteDTO> pacienteList = pacienteService.listarPacientes();
        return ResponseEntity.ok(pacienteList);
    }

    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<PacienteDTO> buscarPorCpf(@PathVariable String cpf) {
        log.info("Buscando paciente pelo CPF de: {} ", cpf);
        return ResponseEntity.ok(pacienteService.buscarPaciente(cpf));
    }

    @PostMapping("/cadastro/paciente")
    public ResponseEntity<PacienteDTO> cadastrarPaciente (
            @Valid @RequestBody PacienteDTO pacienteDTO) {
        log.info("Cadastrando novo paciente");
        PacienteDTO pacienteCadsatrado = pacienteService.criarPaciente(pacienteDTO);
        return ResponseEntity.status(HttpStatus.OK).body(pacienteCadsatrado);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<PacienteDTO> atualizarPaciente (
            @PathVariable String cpf,
            @Valid @RequestBody PacienteDTO pacienteDTO) {
        log.info("Atualizando paciente");
        return ResponseEntity.ok(pacienteService.atualizarPaciente(cpf, pacienteDTO));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable String cpf) {
        log.info("Deletando paciente de CPF: {} ", cpf);
        pacienteService.deletarPaciente(cpf);
        return ResponseEntity.noContent().build();
    }
}
