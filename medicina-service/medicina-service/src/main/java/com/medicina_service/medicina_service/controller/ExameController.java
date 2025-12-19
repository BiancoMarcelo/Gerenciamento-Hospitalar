package com.medicina_service.medicina_service.controller;

import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoRequestDTO;
import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoResponseDTO;
import com.medicina_service.medicina_service.dto.modeldto.ExameDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoResponseDTO;
import com.medicina_service.medicina_service.model.Exame;
import com.medicina_service.medicina_service.model.Procedimento;
import com.medicina_service.medicina_service.service.ExameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/exames")
@RequiredArgsConstructor
public class ExameController {

    private final ExameService exameService;

    @PostMapping
    public ResponseEntity<ProcedimentoResponseDTO> criarExame (
            @Valid @RequestBody ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Criando exame para CPF: {}", procedimentoRequestDTO.getCpfPaciente());

        ProcedimentoResponseDTO procedimentoResponseDTO = exameService.criarExame(procedimentoRequestDTO);

        return ResponseEntity.ok(procedimentoResponseDTO);

    }

    @PostMapping("/agendar-horario")
    public ResponseEntity<ConfirmacaoDeCriacaoResponseDTO> marcarHorario (
            @Valid @RequestBody ConfirmacaoDeCriacaoRequestDTO confirmacaoDeCriacaoRequestDTO) {
        log.info("Agendando horário para exame de Id: {}", confirmacaoDeCriacaoRequestDTO.getProcedimentoId());

        ConfirmacaoDeCriacaoResponseDTO confirmacaoDeCriacaoResponseDTO = exameService
                .marcarHorario(confirmacaoDeCriacaoRequestDTO);

        return ResponseEntity.ok(confirmacaoDeCriacaoResponseDTO);

    }

    @GetMapping("/{cpf}")
    public ResponseEntity<List<ExameDTO>> listarExamesPorCpf (
            @PathVariable String cpf) {
        log.info("Buscando agendamentos para o CPF: {}", cpf);
        List<ExameDTO> exames = exameService.listarPorCpf(cpf);

        return ResponseEntity.ok(exames);
    }

    @GetMapping
    public ResponseEntity<List<ExameDTO>> listarTodosExames() {
        log.info("Listando todos os exames");
        List<ExameDTO> exames = exameService.listarExames();
        return ResponseEntity.ok(exames);
    }

    @GetMapping("/aguardando-agendamento")
    public ResponseEntity<List<ExameDTO>> listarExamesAguardandoAgendamento() {
        log.info("Listando todos os exames aguardando agendamento");
        List<ExameDTO> exames = exameService.listarAguardandoAgendamento();
        return ResponseEntity.ok(exames);
    }

    @PutMapping("/encerrar/{id}")
    public ResponseEntity<Void> encerrarExame (
            @PathVariable Long id,
            @Valid @RequestBody ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Encerrando procedimento de id: {}", id);
        exameService.encerrarExame(id);
        return ResponseEntity.noContent().build();
    }
}
