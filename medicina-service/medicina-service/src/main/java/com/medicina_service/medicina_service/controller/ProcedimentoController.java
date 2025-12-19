package com.medicina_service.medicina_service.controller;

import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoRequestDTO;
import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoResponseDTO;
import com.medicina_service.medicina_service.dto.modeldto.ProcedimentoDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoResponseDTO;
import com.medicina_service.medicina_service.model.Procedimento;
import com.medicina_service.medicina_service.service.ProcedimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    @PostMapping
    public ResponseEntity<ProcedimentoResponseDTO> criarProcedimento (
            @Valid @RequestBody ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Criando procedimento para CPF: {}", procedimentoRequestDTO.getCpfPaciente());

        ProcedimentoResponseDTO procedimentoResponseDTO = procedimentoService.criarProcedimento(procedimentoRequestDTO);

        return ResponseEntity.ok(procedimentoResponseDTO);

    }

    @PostMapping("/agendar-horario")
    public ResponseEntity<ConfirmacaoDeCriacaoResponseDTO> marcarHorario (
            @Valid @RequestBody ConfirmacaoDeCriacaoRequestDTO confirmacaoDeCriacaoRequestDTO) {
        log.info("Agendando horário para procedimento de Id: {}", confirmacaoDeCriacaoRequestDTO.getProcedimentoId());

        ConfirmacaoDeCriacaoResponseDTO confirmacaoDeCriacaoResponseDTO = procedimentoService
                .marcarHorario(confirmacaoDeCriacaoRequestDTO);

        return ResponseEntity.ok(confirmacaoDeCriacaoResponseDTO);

    }

    @GetMapping("/{cpf}")
    public ResponseEntity<List<ProcedimentoDTO>> listarProcedimentosPorCpf (
            @PathVariable String cpf) {
        log.info("Buscando agendamentos para o CPF: {}", cpf);
        List<ProcedimentoDTO> procedimentos = procedimentoService.listarPorCpf(cpf);

        return ResponseEntity.ok(procedimentos);
    }

    @GetMapping
    public ResponseEntity<List<ProcedimentoDTO>> listarTodosProcedimentos() {
        log.info("Listando todos os procedimentos");
        List<ProcedimentoDTO> procedimentos = procedimentoService.listarProcedimentos();
        return ResponseEntity.ok(procedimentos);
    }

    @GetMapping("/aguardando-agendamento")
    public ResponseEntity<List<ProcedimentoDTO>> listarProcedimentosAguardandoAgendamento() {
        log.info("Listando todos os procedimentos aguardando agendamento");
        List<ProcedimentoDTO> procedimentos = procedimentoService.listarAguardandoAgendamento();
        return ResponseEntity.ok(procedimentos);
    }

    @PutMapping("/encerrar/{id}")
    public ResponseEntity<Void> encerrarProcedimento (
            @PathVariable Long id,
            @Valid @RequestBody ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Encerrando procedimento de id: {}", id);
        procedimentoService.encerrarProcedimento(id);
        return ResponseEntity.noContent().build();
    }

}
