package com.agendamento_service.agendamento_service.controller;

import com.agendamento_service.agendamento_service.dto.pacientedto.PacienteDTO;
import com.agendamento_service.agendamento_service.service.PacienteService;
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

import java.awt.image.RescaleOp;
import java.util.List;

@Tag(name = "Paciente", description = "Endpoints para criação e gerenciamento de pacientes")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @Operation(summary = "Listar Pacientes",
            description = "Lista todos os pacientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes listados com sucesso"),
    })
    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDTO>> buscarPacientes() {
        log.info("Buscando todos os pacientes");
        List<PacienteDTO> pacienteList = pacienteService.listarPacientes();
        return ResponseEntity.ok(pacienteList);
    }

    @Operation(summary = "Buscar paciente por CPF",
            description = "Encontra um paciente específico a partir do seu CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientes listados com sucesso"),
    })
    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<PacienteDTO> buscarPorCpf(@PathVariable String cpf) {
        log.info("Buscando paciente pelo CPF de: {} ", cpf);
        return ResponseEntity.ok(pacienteService.buscarPaciente(cpf));
    }

    @Operation(summary = "Cadastrar Novo Paciente",
            description = "Cadastra um novo Paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe no banco de dados")
    })
    @PostMapping("/cadastro/paciente")
    public ResponseEntity<PacienteDTO> cadastrarPaciente (
            @Valid @RequestBody PacienteDTO pacienteDTO) {
        log.info("Cadastrando novo paciente");
        PacienteDTO pacienteCadsatrado = pacienteService.criarPaciente(pacienteDTO);
        return ResponseEntity.status(HttpStatus.OK).body(pacienteCadsatrado);
    }


    @Operation(summary = "Atualizar Dados do Paciente",
            description = "Atualiza os dados de um Paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Dados inválidos")
    })
    @PutMapping("/{cpf}")
    public ResponseEntity<PacienteDTO> atualizarPaciente (
            @PathVariable String cpf,
            @Valid @RequestBody PacienteDTO pacienteDTO) {
        log.info("Atualizando paciente");
        return ResponseEntity.ok(pacienteService.atualizarPaciente(cpf, pacienteDTO));
    }


    @Operation(summary = "Deletar Paciente",
            description = "Deleta um paciente do banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente deletado com sucesso"),
    })
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable String cpf) {
        log.info("Deletando paciente de CPF: {} ", cpf);
        pacienteService.deletarPaciente(cpf);
        return ResponseEntity.noContent().build();
    }
}
