package com.agendamento_service.agendamento_service.service;

import com.agendamento_service.agendamento_service.dto.pacientedto.PacienteDTO;
import com.agendamento_service.agendamento_service.exception.custom.ConflictException;
import com.agendamento_service.agendamento_service.exception.custom.ResourceNotFoundException;
import com.agendamento_service.agendamento_service.mapper.PacienteMapper;
import com.agendamento_service.agendamento_service.messaging.event.EventoPublisher;
import com.agendamento_service.agendamento_service.model.Paciente;
import com.agendamento_service.agendamento_service.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final EmailService emailService;

    public PacienteDTO criarPaciente(PacienteDTO pacienteDTO) {
        if (pacienteRepository.existsByCpf(pacienteDTO.getCpf())) {
            throw new ConflictException("Paciente já existe em sistema!");
        }
        Paciente pacienteEntity = pacienteRepository.save(pacienteMapper.toEntity(pacienteDTO));

        emailService.enviarEmailCriacaoUsuario(pacienteDTO.getEmail(), pacienteDTO.getNome());

        return pacienteMapper.toGlobalDTO(pacienteEntity);
    }

    public List<PacienteDTO> listarPacientes() {
        log.info("Listando todos os pacientes");
        List<Paciente> pacienteList = pacienteRepository.findAll();
        return pacienteList.stream()
                .map(pacienteMapper::toGlobalDTO)
                .toList();
    }

    public PacienteDTO buscarPaciente(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .map(pacienteMapper::toGlobalDTO)
                .orElseThrow(() -> {
                    log.info("Paciente não encontrado em sistema pelo CPF: {} ", cpf);
                    return new ResourceNotFoundException("Paciente não encontrado pelo CPF: " + cpf);
                });
    }

    public Paciente buscarPacienteBancoDados(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .orElseThrow(()-> {
                    log.info("Paciente não encontrado pelo CPF: {} ", cpf);
                    return new ResourceNotFoundException("Paciente não encontrado no banco de dados pelo CPF: " + cpf);
                });
    }

    @Transactional
    public PacienteDTO atualizarPaciente(String cpf, PacienteDTO pacienteDTO) {
        Paciente pacienteAtualizar = pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> {
                    log.info("Paciente não encontrado pelo CPF: {}", cpf);
                    return new ResourceNotFoundException("Paciente não encontrado pelo CPF: " + cpf);
                });
        pacienteAtualizar.setCpf(pacienteDTO.getCpf());
        pacienteAtualizar.setNome(pacienteDTO.getNome());
        pacienteAtualizar.setSexo(pacienteDTO.getSexo());
        pacienteAtualizar.setIdade(pacienteDTO.getIdade());

        Paciente pacienteSalvo = pacienteRepository.save(pacienteMapper.toEntity(pacienteDTO));
        return pacienteMapper.toDTO(pacienteSalvo);
    }

    @Transactional
    public void deletarPaciente(String cpf) {
        pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> {
                    log.info("Não encontrado pelo CPF informado");
                    return new ResourceNotFoundException("Paciente não encontrado pelo CPF: " + cpf);
                });
        pacienteRepository.deleteByCpf(cpf);
        log.info("Paciente deletado");
    }



}
