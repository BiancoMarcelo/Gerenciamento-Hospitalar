package com.agendamento_service.agendamento_service.service;

import com.agendamento_service.agendamento_service.dto.PacienteDTO;
import com.agendamento_service.agendamento_service.mapper.PacienteMapper;
import com.agendamento_service.agendamento_service.messaging.event.EventoPublisher;
import com.agendamento_service.agendamento_service.model.Paciente;
import com.agendamento_service.agendamento_service.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final EventoPublisher eventoPublisher;

    public PacienteDTO criarPaciente(PacienteDTO pacienteDTO) {
        if (pacienteRepository.existsByCpf(pacienteDTO.getCpf())) {
            throw new RuntimeException("Paciente já existe em sistema!");
        }
        Paciente pacienteEntity = pacienteRepository.save(pacienteMapper.toEntity(pacienteDTO));
        return pacienteMapper.toDTO(pacienteEntity);
    }

    public List<PacienteDTO> listarPacientes() {
        log.info("Listando todos os pacientes");
        List<Paciente> pacienteList = pacienteRepository.findAll();
        return pacienteList.stream()
                .map(pacienteMapper::toDTO)
                .toList();
    }

    public PacienteDTO buscarPaciente(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .map(pacienteMapper::toDTO)
                .orElseThrow(() -> {
                    log.info("Paciente não encontrado em sistema pelo CPF: {} ", cpf);
                    return new RuntimeException("Paciente não encontrado!");
                });
    }

    public Paciente buscarPacienteBancoDados(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .orElseThrow(()-> {
                    log.info("Paciente não encontrado pelo CPF: {} ", cpf);
                    return new RuntimeException("Paciente não encontrado pelo CPF!");
                });
    }



}
