package com.agendamento_service.agendamento_service.service;

import com.agendamento_service.agendamento_service.client.ClinicaClient;
import com.agendamento_service.agendamento_service.client.MedicinaClient;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoConsultaRequestDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoExameRequestDTO;
import com.agendamento_service.agendamento_service.dto.agendamentodto.AgendamentoResponseDTO;
import com.agendamento_service.agendamento_service.exception.custom.BadRequestException;
import com.agendamento_service.agendamento_service.exception.custom.ConflictException;
import com.agendamento_service.agendamento_service.exception.custom.ResourceNotFoundException;
import com.agendamento_service.agendamento_service.mapper.AgendamentoMapper;
import com.agendamento_service.agendamento_service.messaging.event.EventoPublisher;
import com.agendamento_service.agendamento_service.model.Agendamento;
import com.agendamento_service.agendamento_service.model.Paciente;
import com.agendamento_service.agendamento_service.model.TipoAgendamento;
import com.agendamento_service.agendamento_service.repository.AgendamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoMapper agendamentoMapper;
    private final EventoPublisher eventoPublisher;
    private final PacienteService pacienteService;
    private final MedicinaClient medicinaClient;
    private final ClinicaClient clinicaClient;
    private final EmailService emailService;

    @Transactional
    public AgendamentoResponseDTO agendarConsulta(AgendamentoConsultaRequestDTO agendamentoConsultaRequestDTO) {
        log.info("Log de info para a especialidade: {}", agendamentoConsultaRequestDTO.getMedico());

        if (!clinicaClient.validarConsulta(agendamentoConsultaRequestDTO.getMedico(), agendamentoConsultaRequestDTO.getHorario())) {
            throw new BadRequestException("Tipo de consulta inválida: " + agendamentoConsultaRequestDTO.getMedico());
        }
        log.info("Agendando consulta para o paciente: {}, CPF: {} ",
                agendamentoConsultaRequestDTO.getPaciente().getNome(), agendamentoConsultaRequestDTO.getPaciente().getCpf());

        Paciente paciente = pacienteService.buscarPacienteBancoDados(agendamentoConsultaRequestDTO.getPaciente().getCpf());

        validarHorarioPaciente(paciente.getCpf(), agendamentoConsultaRequestDTO.getHorario());
        validarMedicoDisponivel(agendamentoConsultaRequestDTO.getMedico(), agendamentoConsultaRequestDTO.getHorario());

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamentoMapper.toEntityConsulta(agendamentoConsultaRequestDTO, paciente));

        eventoPublisher.publicarConsulta(agendamentoSalvo);

        log.info("Consulta agendada com sucesso. Id: {} ", agendamentoSalvo.getId());

        emailService.enviarEmailConsultaAgendada(
                agendamentoConsultaRequestDTO.getPaciente().getEmail(),
                agendamentoConsultaRequestDTO.getPaciente().getNome(),
                agendamentoConsultaRequestDTO.getMedico(),
                agendamentoSalvo.getHorario().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                agendamentoSalvo.getId().toString()
        );

        return agendamentoMapper.toAgendamentoConsultaResponseDTO(agendamentoSalvo);

    }

    @Transactional
    public AgendamentoResponseDTO agendarExame(AgendamentoExameRequestDTO agendamentoExameRequestDTO) {

        if (!medicinaClient.exameExiste(agendamentoExameRequestDTO.getExame())) {
            throw new BadRequestException("Tipo de exame invalido: " + agendamentoExameRequestDTO.getExame());
        }
        log.info("Agendando consulta exame {} para o paciente {} ",
                agendamentoExameRequestDTO.getExame(), agendamentoExameRequestDTO.getPaciente().getNome());

        Paciente paciente = pacienteService.buscarPacienteBancoDados(agendamentoExameRequestDTO.getPaciente().getCpf());

        validarHorarioPaciente(agendamentoExameRequestDTO.getPaciente().getCpf(), agendamentoExameRequestDTO.getHorario());
        validarExameDisponivel(agendamentoExameRequestDTO.getExame(), agendamentoExameRequestDTO.getHorario());

        Agendamento agendamento = agendamentoRepository.save(agendamentoMapper.toEntityExame(agendamentoExameRequestDTO, paciente));

        eventoPublisher.publicarExame(agendamento);

        emailService.enviarEmailExameAgendado(
                agendamentoExameRequestDTO.getPaciente().getEmail(),
                agendamentoExameRequestDTO.getPaciente().getNome(),
                agendamentoExameRequestDTO.getExame(),
                agendamento.getHorario().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                agendamento.getId().toString()
        );

        return agendamentoMapper.toAgendamentoExameResponseDTO(agendamento);

    }

    @Cacheable(value = "agendamentos", key = "#cpf")
    public List<AgendamentoDTO> buscarPorCPF(String cpf) {
        log.info("Buscando agendamentos disponíveis para o CPF: {} ", cpf);

        if (!agendamentoRepository.existsByPaciente_Cpf(cpf)) {
            log.info("Não encontrado pelo CPF: {}", cpf);
            throw new ResourceNotFoundException("Paciente não encontrado pelo CPF: " + cpf);
        }

        return agendamentoRepository.findByPaciente_Cpf(cpf).stream()
                .map((agendamento)-> {
                    AgendamentoDTO dto = agendamentoMapper.toAgendamentoDTO(agendamento);
                    return dto;
                }).toList();
    }

    @Transactional
    public AgendamentoResponseDTO atualizarConsulta(Long id, String cpf, AgendamentoConsultaRequestDTO agendamentoConsultaRequestDTO) {
        log.info("Buscando consulta {} do usuário de CPF: {} ", id, cpf);

        Agendamento agendamentoExistente = agendamentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Agendamento não encontrado pelo id: {} ", id);
                    return new ResourceNotFoundException("Consulta não encontrada pelo ID: " + id);
                });
        if (!agendamentoExistente.getPaciente().getCpf().equals(cpf)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esse agendamento não pertence ao CPF informado");
        }

        validarHorarioPaciente(agendamentoConsultaRequestDTO.getPaciente().getCpf(), agendamentoConsultaRequestDTO.getHorario());
        validarMedicoDisponivel(agendamentoConsultaRequestDTO.getMedico(), agendamentoConsultaRequestDTO.getHorario());

        agendamentoExistente.setEspecialidade(agendamentoConsultaRequestDTO.getMedico());
        agendamentoExistente.setHorario(agendamentoConsultaRequestDTO.getHorario());

        Agendamento agendamentoAtualizado = agendamentoRepository.save(agendamentoExistente);
        log.info("Agendamento atualizado para: consulta com {} na data {} ", agendamentoAtualizado.getEspecialidade(), agendamentoAtualizado.getHorario());

        return AgendamentoResponseDTO.builder()
                .mensagem(String.format("O %s de %s foi atualizado para %s ",
                        agendamentoConsultaRequestDTO.getMedico(),
                        agendamentoConsultaRequestDTO.getPaciente().getNome(),
                        agendamentoConsultaRequestDTO.getHorario()))
                .codigo(agendamentoAtualizado.getId().toString())
                .build();

    }

    @Transactional
    public AgendamentoResponseDTO atualizarExame(Long id, String cpf, AgendamentoExameRequestDTO agendamentoExameRequestDTO) {
        log.info("Buscando exame {} do usuário de CPF: {} ", id, cpf);

        Agendamento agendamentoExistente = agendamentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Exame não encontrado pelo id: {} ", id);
                    return new ResourceNotFoundException("Consulta não encontrada pelo ID: " + id);
                });
        if (!agendamentoExistente.getPaciente().getCpf().equals(cpf)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese agendamento não pertence ao CPF informado");
        }

        validarHorarioPaciente(agendamentoExameRequestDTO.getPaciente().getCpf(), agendamentoExameRequestDTO.getHorario());
        validarExameDisponivel(agendamentoExameRequestDTO.getExame(), agendamentoExameRequestDTO.getHorario());

        agendamentoExistente.setTipoExame(agendamentoExameRequestDTO.getExame());
        agendamentoExistente.setHorario(agendamentoExameRequestDTO.getHorario());

        Agendamento agendamentoAtualizado = agendamentoRepository.save(agendamentoExistente);
        log.info("Agendamento atualizado para: exame de {} na data {} ", agendamentoAtualizado.getTipoExame(), agendamentoAtualizado.getHorario());

        return AgendamentoResponseDTO.builder()
                .mensagem(String.format("O %s de %s foi marcado para %s ",
                        agendamentoExameRequestDTO.getExame(),
                        agendamentoExameRequestDTO.getPaciente().getNome(),
                        agendamentoExameRequestDTO.getHorario()))
                .codigo(agendamentoAtualizado.getId().toString())
                .build();

    }

    @Transactional
    public void deletarAgendaemento(Long id) {
        log.info("Deletando consulta de Id: {} ", id);

        Agendamento agendamentoExistente = agendamentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Agendamento não encontrado pelo id: {} ", id);
                    return new ResourceNotFoundException("Consulta não encontrada pelo ID: " + id);
                });

        agendamentoRepository.deleteById(id);

        if (agendamentoExistente.getTipoAgendamento() == TipoAgendamento.EXAME) {
            log.info("Tipo de exame: {}", agendamentoExistente.getTipoAgendamento() );
            eventoPublisher.publicarExclusaoExame(id);
        } else if (agendamentoExistente.getTipoAgendamento() == TipoAgendamento.CONSULTA) {
            log.info("Tipo de consulta: {}", agendamentoExistente.getTipoAgendamento() );
            eventoPublisher.publicarExclusaoConsulta(id);
        } else {
            throw new BadRequestException("Não foi possivel cancelar o agendamento");
        }

        log.info("Agendamento cancelado!");
    }

    @Transactional
    public void deletarAgendamentos(){
        log.info("Deletando todos agendamentos");
        agendamentoRepository.deleteAll();
    }

    @Cacheable(value = "agendamentos")
    public List<AgendamentoDTO> listarTodosAgendamentos() {
        log.info("Listando todos os agendamentos");
        return agendamentoRepository.findAll().stream()
                .map((agendamento -> {
                    AgendamentoDTO dto = agendamentoMapper.toAgendamentoDTO(agendamento);
                    return dto;
                })).toList();

    }

    private void validarHorarioPaciente (String cpf, java.time.LocalDateTime horario) {
        if (agendamentoRepository.existsByPaciente_CpfAndHorario(cpf, horario)) {
            log.warn("Conflito de agendamento: Paciente {} já possui agendamento no horário {} ", cpf, horario);
            throw new ConflictException("O paciente já possui agendamento no horário: " +  horario);

        }
    }

    private void validarMedicoDisponivel(String especialidade, java.time.LocalDateTime horario) {
        if (agendamentoRepository.existsByEspecialidadeAndHorarioAndTipoAgendamento(
                especialidade, horario, TipoAgendamento.CONSULTA)) {
            log.warn("Conflito: Médico {} já ocupado em {} ", especialidade, horario);
            throw new ConflictException("Médico da especialidade " + especialidade + " não está disponível no horário: " + horario);

        }

        boolean disponivelNaClinica = clinicaClient.verificarDisponibilidadeMedico(especialidade, horario);

        if (!disponivelNaClinica) {
            log.warn("Conflito: médico {} já ocupado em {} na clíica", especialidade, horario);
            throw new ConflictException("Médico da especialidade " + especialidade + " não está disponível no horário: " + horario);
        }

        log.info("Médico {} disponível no horário: {} ", especialidade, horario);
    }

    private void validarExameDisponivel(String tipoExame, java.time.LocalDateTime horario) {
        if (agendamentoRepository.existsByTipoExameAndHorarioAndTipoAgendamento(
                tipoExame, horario, TipoAgendamento.EXAME)) {
            log.warn("Conflito: Exame {} já agendado em {} ", tipoExame, horario);
            throw new ConflictException("Exame do tipo " + tipoExame + " não está disponível no horário: " + horario);

        }
    }
}
