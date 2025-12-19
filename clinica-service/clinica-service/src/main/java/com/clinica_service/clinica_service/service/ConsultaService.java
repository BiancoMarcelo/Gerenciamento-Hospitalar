package com.clinica_service.clinica_service.service;

import com.clinica_service.clinica_service.dto.consultadto.ConsultaDTO;
import com.clinica_service.clinica_service.dto.consultadto.verificarconsulta.VerificarConsultaRequestDTO;
import com.clinica_service.clinica_service.dto.consultadto.verificarconsulta.VerificarConsultaResponseDTO;
import com.clinica_service.clinica_service.exception.custom.ResourceNotFoundException;
import com.clinica_service.clinica_service.mapper.ConsultaMapper;
import com.clinica_service.clinica_service.model.Consulta;
import com.clinica_service.clinica_service.model.StatusConsulta;
import com.clinica_service.clinica_service.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;

    @Transactional
    public void processarConsultaAgendada(ConsultaDTO consultaDTO) {
        log.info("Processando consulta recebida do agendamento-service, Agendamento ID: {} ", consultaDTO.getAgendamentoId());

        Optional<Consulta> consultaExistente = consultaRepository.findByAgendamentoId(consultaDTO.getAgendamentoId());
        if (consultaExistente.isPresent()) {
            log.warn("Consulta já existe para o agendamento ID: {} ", consultaDTO.getAgendamentoId());
        }

        Consulta consulta = consultaMapper.toEntity(consultaDTO);
        consultaRepository.save(consulta);

        log.info("Consulta cadastrada com sucesso! CPF: {}, Horário: {}", consulta.getCpfPaciente(), consulta.getHorario());

    }

    public VerificarConsultaResponseDTO verificarDisponibilidadeMedico
            (VerificarConsultaRequestDTO verificarConsultaRequestDTO) {
        log.info("Verificando disponibilidade do médico {} no horário {} ",
                verificarConsultaRequestDTO.getEspecialidade(), verificarConsultaRequestDTO.getHorario());

        boolean ocupado = consultaRepository.existsByEspecialidadeMedicoAndHorario(
                verificarConsultaRequestDTO.getEspecialidade(), verificarConsultaRequestDTO.getHorario());

        if (ocupado) {
            return VerificarConsultaResponseDTO.builder()
                    .disponivel(false)
                    .mensagem(String.format("O médico %s já possui agendamento no horário solicitado %s",
                            verificarConsultaRequestDTO.getEspecialidade(),
                            verificarConsultaRequestDTO.getHorario()))
                    .build();
        }

        return VerificarConsultaResponseDTO.builder()
                .disponivel(true)
                .mensagem("Horário disponível")
                .build();
    }


    public List<ConsultaDTO> listarPorCPF(String cpf) {
        log.info("Buscando consultas do CPF: {} ", cpf);
        return consultaRepository.findByCpfPaciente(cpf)
                .stream()
                .map(consultaMapper::toDTO)
                .toList();
    }

    public List<ConsultaDTO> listarTodasConsultas() {
        log.info("Listando todas as consultas");
        return consultaRepository.findAll()
                .stream()
                .map(consultaMapper::toDTO)
                .toList();
    }

    public List<Consulta> listarTodasConsultasAgendadas() {
        log.info("Listando todas as consultas com status de 'agendado'");
        return consultaRepository.findByStatus(StatusConsulta.AGENDADA);
    }

    @Transactional
    public void atualizarStatusConsulta(Long consultaId, StatusConsulta novoStatus) {
        Consulta consulta = buscarPorId(consultaId);
        consulta.setStatus(novoStatus);
        consultaRepository.save(consulta);
        log.info("Consulta de id: {} atualizada para Status de: {} ", consultaId, novoStatus);
    }

    public Consulta buscarPorCpfEHorario(String cpf, LocalDateTime horario) {
        return consultaRepository.findByCpfPacienteAndHorario(cpf, horario)
                .orElseThrow(()->{
                    log.error("Consulta não encontrada para o CPF {} no horário {}", cpf, horario);
                    return new ResourceNotFoundException("Consulta não encontrada");
                });
    }

    public Consulta buscarPorId(Long id) {
        log.info("Buscando consulta por Id: {} ", id);
        return consultaRepository.findById(id)
                .orElseThrow(()-> {
                    log.info("Consulta não encontrada pelo id: {} ", id);
                    return new ResourceNotFoundException("Não encontrado pelo id fornecido");
                });
    }
}
