package com.agendamento_service.agendamento_service.controller;

import com.agendamento_service.agendamento_service.dto.AgendamentoConsultaRequestDTO;
import com.agendamento_service.agendamento_service.dto.AgendamentoExameRequestDTO;
import com.agendamento_service.agendamento_service.dto.AgendamentoResponseDTO;
import com.agendamento_service.agendamento_service.model.Agendamento;
import com.agendamento_service.agendamento_service.service.AgendamentoService;
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

        log.info("Recebendo requisição para agendar consulta");
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

    @GetMapping("agendamentos/{cpf}")
    public ResponseEntity<List<Agendamento>> consultarAgendamentos(@PathVariable String cpf) {
        log.info("Buscando agendamentos do CPF: {} ", cpf);
        List<Agendamento> agendamentos = agendamentoService.buscarPorCPF(cpf);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("agendamentos")
    public ResponseEntity<List<Agendamento>> consultarTodosAgendamentos() {
        log.info("Buscando todos agendamentos");
        List<Agendamento> agendamentos = agendamentoService.listarTodosAgendamentos();
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
}
