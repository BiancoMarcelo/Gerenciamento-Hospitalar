package com.clinica_service.clinica_service.controller;


import com.clinica_service.clinica_service.dto.consultadto.ConsultaDTO;
import com.clinica_service.clinica_service.dto.consultadto.verificarconsulta.VerificarConsultaRequestDTO;
import com.clinica_service.clinica_service.dto.consultadto.verificarconsulta.VerificarConsultaResponseDTO;
import com.clinica_service.clinica_service.model.Consulta;
import com.clinica_service.clinica_service.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clinica")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;


    @PostMapping("/verificar-disponibilidade")
    public ResponseEntity<VerificarConsultaResponseDTO> verificarDisponibilidadeDeConsulta(
            @Valid @RequestBody VerificarConsultaRequestDTO requestDTO) {
        log.info("Verificando disponibilidade para consulta de especilidade: {}, no horário: {}", requestDTO.getEspecialidade(), requestDTO.getHorario());

        VerificarConsultaResponseDTO responseDTO = consultaService.verificarDisponibilidadeMedico(requestDTO);

        if (!responseDTO.isDisponivel()) {
            return ResponseEntity.status(409).body(responseDTO);
        }

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/consultas/{cpf}")
    public ResponseEntity<List<ConsultaDTO>> listarPorCpf(@PathVariable String cpf) {
        log.info("Listando consultas do CPF: {}", cpf);
        List<ConsultaDTO> consultas = consultaService.listarPorCPF(cpf);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/consultas/agendadas")
    public ResponseEntity<List<Consulta>> listarAgendadas() {
        log.info("Listando todas consultas agendadas");
        List<Consulta> consultas = consultaService.listarTodasConsultasAgendadas();
        return ResponseEntity.ok(consultas);
    }


}
