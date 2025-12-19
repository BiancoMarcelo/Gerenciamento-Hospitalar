package com.agendamento_service.agendamento_service.controller;

import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoConsultaRequestDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoExameRequestDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoResponseDTO;
import com.agendamento_service.agendamento_service.dto.pacientedto.PacienteDTO;
import com.agendamento_service.agendamento_service.model.Agendamento;
import com.agendamento_service.agendamento_service.service.AgendamentoService;
import com.agendamento_service.agendamento_service.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping("/cadastro/consulta")
    public ResponseEntity<AgendamentoResponseDTO> agendarConsulta (
            @Valid @RequestBody AgendamentoConsultaRequestDTO agendamentoConsultaRequestDTO) {

        log.info("Recebendo requisição para agendar consulta em AgendamentoController");
        AgendamentoResponseDTO responseDTO = agendamentoService.agendarConsulta(agendamentoConsultaRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping("/cadastro/exame")
    public ResponseEntity<AgendamentoResponseDTO> agendarExame (
            @Valid @RequestBody AgendamentoExameRequestDTO agendamentoExameRequestDTO) {

        log.info("Recebendo requisição para agendar exame");
        AgendamentoResponseDTO responseDTO = agendamentoService.agendarExame(agendamentoExameRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/agendamentos/{cpf}")
    public ResponseEntity<List<AgendamentoDTO>> consultarAgendamentos(@PathVariable String cpf) {
        log.info("Buscando agendamentos do CPF: {} ", cpf);
        List<AgendamentoDTO> agendamentos = agendamentoService.buscarPorCPF(cpf);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/agendamentos")
    public ResponseEntity<List<AgendamentoDTO>> consultarTodosAgendamentos() {
        log.info("Buscando todos agendamentos");
        List<AgendamentoDTO> agendamentos = agendamentoService.listarTodosAgendamentos();
        return ResponseEntity.ok(agendamentos);
    }

    @PutMapping("/consulta/atualizar/{cpf}/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizarConsulta(
            @PathVariable Long id,
            @PathVariable String cpf,
            @Valid @RequestBody AgendamentoConsultaRequestDTO agendamentoConsultaRequestDTO) {
        log.info("Atualizando consulta do CPF: {} de Id: {} ", cpf, id);
        return ResponseEntity.ok(agendamentoService.atualizarConsulta(id, cpf, agendamentoConsultaRequestDTO));
    }

    @PutMapping("/exame/atualizar/{cpf}/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizarExame(
            @PathVariable Long id,
            @PathVariable String cpf,
            @Valid @RequestBody AgendamentoExameRequestDTO agendamentoExameRequestDTO) {
        log.info("Atualizando exame do CPF: {} de Id: {} ", cpf, id);
        return ResponseEntity.ok(agendamentoService.atualizarExame(id, cpf, agendamentoExameRequestDTO));
    }

    @DeleteMapping("/agendamentos/{cpf}/{id}")
    public ResponseEntity<Void> deletarAgendamento(
            @PathVariable Long id,
            @PathVariable String cpf) {
        log.info("Deletando agendamento do CPF: {} de Id: {} ", cpf, id);
        agendamentoService.deletarAgendaemento(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/agendamentos")
    public ResponseEntity<Void> deletarTodosAgendamentos() {
        log.info("Deletando todos os agendamentos");
        agendamentoService.deletarAgendamentos();
        return ResponseEntity.noContent().build();
    }
}
