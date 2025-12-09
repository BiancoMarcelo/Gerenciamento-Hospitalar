package com.agendamento_service.agendamento_service.service;

import com.agendamento_service.agendamento_service.dto.*;
import com.agendamento_service.agendamento_service.mapper.AgendamentoMapper;
import com.agendamento_service.agendamento_service.mapper.PacienteMapper;
import com.agendamento_service.agendamento_service.messaging.EventoPublisher;
import com.agendamento_service.agendamento_service.model.Agendamento;
import com.agendamento_service.agendamento_service.model.Paciente;
import com.agendamento_service.agendamento_service.model.TipoAgendamento;
import com.agendamento_service.agendamento_service.repository.AgendamentoRepository;
import com.agendamento_service.agendamento_service.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendamentoMapper agendamentoMapper;
    private final PacienteMapper pacienteMapper;
    private final EventoPublisher eventoPublisher;

    @Transactional
    public AgendamentoResponseDTO agendarConsulta (AgendamentoConsultaRequestDTO agendamentoConsultaRequestDTO) {
        log.info("Agendando consulta para o paciente: {}, CPF: {} ",
                agendamentoConsultaRequestDTO.getPaciente().getNome(), agendamentoConsultaRequestDTO.getPaciente().getCpf());

        Paciente paciente = buscarOuCriarPaciente(agendamentoConsultaRequestDTO.getPaciente());

        validarHorarioPaciente(paciente.getCpf(), agendamentoConsultaRequestDTO.getHorario());
        validarMedicoDisponivel(agendamentoConsultaRequestDTO.getMedico(), agendamentoConsultaRequestDTO.getHorario());

        Agendamento agendamento = agendamentoMapper.toEntityConsulta(agendamentoConsultaRequestDTO, paciente);
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        eventoPublisher.publicarConsulta(agendamentoSalvo);

        log.info("Consulta agendada com sucesso. Id: {} ", agendamentoSalvo.getId());

        return AgendamentoResponseDTO.builder()
                .mensagem(String.format("O %s de %s foi marcado para %s ",
                        agendamentoConsultaRequestDTO.getMedico(),
                        agendamentoConsultaRequestDTO.getPaciente().getNome(),
                        agendamentoConsultaRequestDTO.getHorario()))
                .codigo(agendamento.getId().toString())
                .build();
    }

    @Transactional
    public AgendamentoResponseDTO agendarExame(AgendamentoExameRequestDTO agendamentoExameRequestDTO){
        log.info("Agendando consulta exame {} para o paciente {} ",
                agendamentoExameRequestDTO.getExame(), agendamentoExameRequestDTO.getPaciente().getNome());

        Paciente paciente = buscarOuCriarPaciente(agendamentoExameRequestDTO.getPaciente());

        validarHorarioPaciente(agendamentoExameRequestDTO.getPaciente().getCpf(), agendamentoExameRequestDTO.getHorario());
        validarExameDisponivel(agendamentoExameRequestDTO.getExame(), agendamentoExameRequestDTO.getHorario());

        Agendamento agendamento = agendamentoMapper.toEntityExame(agendamentoExameRequestDTO, paciente);
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        eventoPublisher.publicarExame(agendamentoSalvo);

        return AgendamentoResponseDTO.builder()
                .mensagem(String.format("O %s de %s foi marcado para %s ",
                        agendamentoExameRequestDTO.getExame(),
                        agendamentoExameRequestDTO.getPaciente().getNome(),
                        agendamentoExameRequestDTO.getHorario()))
                .codigo(agendamento.getId().toString())
                .build();
    }

    public List<Agendamento> buscarPorCPF(String cpf) {
        log.info("Buscando agendamentos disponíveis para o CPF: {} ", cpf);
        return agendamentoRepository.findByPaciente_Cpf(cpf);
    }

    @Transactional
    public AgendamentoResponseDTO atualizarConsulta(Long id, String cpf, AgendamentoConsultaRequestDTO agendamentoConsultaRequestDTO) {
        log.info("Buscando consulta {} do usuário de CPF: {} ", id, cpf);

        Agendamento agendamentoExistente = agendamentoRepository.findById(id)
                .orElseThrow(()-> {
                    log.info("Agendamento não encontrado pelo id: {} ", id);
                    return new RuntimeException("Não foi possível encontrar o agendamento pelo Id fornecido");
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
                .orElseThrow(()-> {
                    log.info("Exame não encontrado pelo id: {} ", id);
                    return new RuntimeException("Não foi possível encontrar o exame pelo Id fornecido");
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
    public void deletarAgendaemento (Long id) {
        log.info("Deletando consulta de Id: {} ", id);

        Agendamento agendamentoExistente = agendamentoRepository.findById(id)
                .orElseThrow(()-> {
                    log.info("Agendamento não encontrado pelo id: {} ", id);
                    return new RuntimeException("Não foi possível encontrar o agendamento pelo Id fornecido");
                });

        agendamentoRepository.deleteById(id);
        log.info("Agendamento deletado!");
    }

    public List<Agendamento> listarTodosAgendamentos() {
        log.info("Listando todos os agendamentos");
        List<Agendamento> agendamentos = agendamentoRepository.findAll();
        return agendamentos.stream().toList();

    }


    private Paciente buscarOuCriarPaciente(PacienteDTO pacienteDTO) {
        return pacienteRepository.findByCpf(pacienteDTO.getCpf())
                .orElseGet(() -> {
                    log.info("Criando novo paciente: {}", pacienteDTO.getNome());
                    Paciente novoPaciente = pacienteMapper.toEntity(pacienteDTO);
                    return pacienteRepository.save(novoPaciente);
                });
    }

    private void validarHorarioPaciente (String cpf, java.time.LocalDateTime horario) {
        if (agendamentoRepository.existsByPaciente_CpfAndHorario(cpf, horario)) {
            log.warn("Conflito de agendamento: Paciente {} já possui agendamento no horário {} ", cpf, horario);
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("O paciente já possui agendamento no horário %s", horario)
            );
        }
    }

    private void validarMedicoDisponivel(String especialidade, java.time.LocalDateTime horario) {
        if (agendamentoRepository.existsByEspecialidadeAndHorarioAndTipoAgendamento(
                especialidade, horario, TipoAgendamento.CONSULTA)) {
            log.warn("Conflito: Médico {} já ocupado em {} ", especialidade, horario);
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("Médico da especialidade %s não está disponível no horário %s", especialidade, horario)
            );
        }
    }

    private void validarExameDisponivel(String tipoExame, java.time.LocalDateTime horario) {
        if (agendamentoRepository.existsByTipoExameAndHorarioAndTipoAgendamento(
                tipoExame, horario, TipoAgendamento.EXAME)) {
            log.warn("Conflito: Exame {} já agendado em {} ", tipoExame, horario);
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("Exame do tipo %s não está disponível no horário %s", tipoExame, horario)
            );
        }
    }
}
