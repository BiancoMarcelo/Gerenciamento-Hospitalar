package com.agendamento_service.agendamento_service.controller;

import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoConsultaRequestDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoExameRequestDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoResponseDTO;
import com.agendamento_service.agendamento_service.dto.pacientedto.PacienteDTO;
import com.agendamento_service.agendamento_service.model.Agendamento;
import com.agendamento_service.agendamento_service.service.AgendamentoService;
import com.agendamento_service.agendamento_service.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Agendamento", description = "Endpoints para agendamento de consultas e exames")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @Operation(summary = "Agendar consulta",
            description = "Cria um novo agendamento de consulta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito de horário")
    })
    @PostMapping("/cadastro/consulta")
    public ResponseEntity<AgendamentoResponseDTO> agendarConsulta (
            @Valid @RequestBody AgendamentoConsultaRequestDTO agendamentoConsultaRequestDTO) {

        log.info("Recebendo requisição para agendar consulta em AgendamentoController");
        AgendamentoResponseDTO responseDTO = agendamentoService.agendarConsulta(agendamentoConsultaRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "Agendar exame",
            description = "Cria um novo agendamento de exame")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exame agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito de horário")
    })
    @PostMapping("/cadastro/exame")
    public ResponseEntity<AgendamentoResponseDTO> agendarExame (
            @Valid @RequestBody AgendamentoExameRequestDTO agendamentoExameRequestDTO) {

        log.info("Recebendo requisição para agendar exame");
        AgendamentoResponseDTO responseDTO = agendamentoService.agendarExame(agendamentoExameRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "Buscar agendamentos por CPF",
            description = "Lista os agendamentos de um CPF especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos encontrados"),
    })
    @GetMapping("/agendamentos/{cpf}")
    public ResponseEntity<List<AgendamentoDTO>> consultarAgendamentos(@PathVariable String cpf) {
        log.info("Buscando agendamentos do CPF: {} ", cpf);
        List<AgendamentoDTO> agendamentos = agendamentoService.buscarPorCPF(cpf);
        return ResponseEntity.ok(agendamentos);
    }


    @Operation(summary = "Buscar Agendamentos",
            description = "Lista todos os agendamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos encontrados")
    })
    @GetMapping("/agendamentos")
    public ResponseEntity<List<AgendamentoDTO>> consultarTodosAgendamentos() {
        log.info("Buscando todos agendamentos");
        List<AgendamentoDTO> agendamentos = agendamentoService.listarTodosAgendamentos();
        return ResponseEntity.ok(agendamentos);
    }

    @Operation(summary = "Atualizar consulta",
            description = "Atualiza uma consulta a partir do CPF do usuário e ID do agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito de horário")
    })
    @PutMapping("/consulta/atualizar/{cpf}/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizarConsulta(
            @PathVariable Long id,
            @PathVariable String cpf,
            @Valid @RequestBody AgendamentoConsultaRequestDTO agendamentoConsultaRequestDTO) {
        log.info("Atualizando consulta do CPF: {} de Id: {} ", cpf, id);
        return ResponseEntity.ok(agendamentoService.atualizarConsulta(id, cpf, agendamentoConsultaRequestDTO));
    }

    @Operation(summary = "Atualizar exame",
            description = "Atualiza uma exame a partir do CPF do usuário e ID do agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exame atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito de horário")
    })
    @PutMapping("/exame/atualizar/{cpf}/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizarExame(
            @PathVariable Long id,
            @PathVariable String cpf,
            @Valid @RequestBody AgendamentoExameRequestDTO agendamentoExameRequestDTO) {
        log.info("Atualizando exame do CPF: {} de Id: {} ", cpf, id);
        return ResponseEntity.ok(agendamentoService.atualizarExame(id, cpf, agendamentoExameRequestDTO));
    }

    @Operation(summary = "Deletar Agendamento",
            description = "Deleta um agendamento a partir do CPF e ID do agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta agendada com sucesso"),
    })
    @DeleteMapping("/agendamentos/{cpf}/{id}")
    public ResponseEntity<Void> deletarAgendamento(
            @PathVariable Long id,
            @PathVariable String cpf) {
        log.info("Deletando agendamento do CPF: {} de Id: {} ", cpf, id);
        agendamentoService.deletarAgendaemento(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deletar Agendamentos",
            description = "Deleta todos os agendamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta agendada com sucesso"),
    })
    @DeleteMapping("/agendamentos")
    public ResponseEntity<Void> deletarTodosAgendamentos() {
        log.info("Deletando todos os agendamentos");
        agendamentoService.deletarAgendamentos();
        return ResponseEntity.noContent().build();
    }
}
