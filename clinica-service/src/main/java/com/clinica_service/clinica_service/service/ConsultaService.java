package com.clinica_service.clinica_service.service;

import com.clinica_service.clinica_service.dto.consultadto.ConsultaDTO;
import com.clinica_service.clinica_service.dto.consultadto.verificarconsulta.VerificarConsultaRequestDTO;
import com.clinica_service.clinica_service.dto.consultadto.verificarconsulta.VerificarConsultaResponseDTO;
import com.clinica_service.clinica_service.exception.custom.ConflictException;
import com.clinica_service.clinica_service.exception.custom.ResourceNotFoundException;
import com.clinica_service.clinica_service.mapper.ConsultaMapper;
import com.clinica_service.clinica_service.model.Consulta;
import com.clinica_service.clinica_service.model.StatusConsulta;
import com.clinica_service.clinica_service.model.TipoConsulta;
import com.clinica_service.clinica_service.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;

    @CacheEvict(value = "consultas", allEntries = true)
    @Transactional
    public void processarConsultaAgendada(ConsultaDTO consultaDTO) {
        log.info("Processando consulta recebida do agendamento-service, Agendamento ID: {} ", consultaDTO.getAgendamentoId());

        Optional<Consulta> consultaExistente = consultaRepository.findByAgendamentoId(consultaDTO.getAgendamentoId());
        if (consultaExistente.isPresent()) {
            log.warn("Consulta já existe para o agendamento ID: {} ", consultaDTO.getAgendamentoId());
        }

        validarHorarioEConsulta(consultaDTO.getEspecialidadeMedico(), consultaDTO.getHorario());

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

    @Cacheable(value = "consultas", key = "#cpf")
    public List<ConsultaDTO> listarPorCPF(String cpf) {
        log.info("Buscando consultas do CPF: {} ", cpf);
        return consultaRepository.findByCpfPaciente(cpf)
                .stream()
                .map(consultaMapper::toDTO)
                .toList();
    }

    @Cacheable(value = "consultas")
    public List<ConsultaDTO> listarTodasConsultas() {
        log.info("Listando todas as consultas");
        return consultaRepository.findAll()
                .stream()
                .map(consultaMapper::toDTO)
                .toList();
    }

    @Cacheable(value = "consultas", key = "'agendadas'")
    public List<Consulta> listarTodasConsultasAgendadas() {
        log.info("Listando todas as consultas com status de 'agendado'");
        return consultaRepository.findByStatus(StatusConsulta.AGENDADA);
    }

    @CacheEvict(value = "consultas", allEntries = true)
    @Transactional
    public void atualizarStatusConsulta(Long consultaId, StatusConsulta novoStatus) {
        Consulta consulta = buscarPorId(consultaId);
        consulta.setStatus(novoStatus);
        consultaRepository.save(consulta);
        log.info("Consulta de id: {} atualizada para Status de: {} ", consultaId, novoStatus);
    }

    public Consulta buscarPorCpfEHorario(String cpf, LocalDateTime horario) {
        return consultaRepository.findByCpfPacienteAndHorario(cpf, horario)
                .orElseThrow(() -> {
                    log.error("Consulta não encontrada para o CPF {} no horário {}", cpf, horario);
                    return new ResourceNotFoundException("Consulta não encontrada");
                });
    }

    public Consulta buscarPorId(Long id) {
        log.info("Buscando consulta por Id: {} ", id);
        return consultaRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Consulta não encontrada pelo id: {} ", id);
                    return new ResourceNotFoundException("Não encontrado pelo id fornecido");
                });
    }

    public ConsultaDTO atualizarConsulta(String cpf, Long id) {
        log.info("Atualizando consulta de ID: {} ", id);
        if (!consultaRepository.existsByCpfPaciente(cpf)) {
            log.info("Não encontrado pelo CPF fornecido");
            throw new ResourceNotFoundException("Não encontrado pelo CPF fornecido");
        }
        atualizarStatusConsulta(id, StatusConsulta.ATENDIDA);
        return consultaMapper.toGenericDTO(buscarPorId(id));

    }

    public void deletarConsultas() {
        log.info("Deletando todas as consultas");
        consultaRepository.deleteAll();
    }

    public void deletarConsulta(Long id) {
        log.info("Deletando consulta de id: {} ", id);
        Consulta consulta = consultaRepository.findById(id)
                .or(() -> consultaRepository.findByAgendamentoId(id))
                .orElseThrow(() -> new RuntimeException("Consulta não encontrado pelo id fornecido"));
        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }

    public void validarHorarioEConsulta(String nomeConsulta, LocalDateTime horario) {
        TipoConsulta tipo = validarConsulta(nomeConsulta);
        validarHorario(horario, tipo);
    }

    public TipoConsulta validarConsulta(String nomeConsulta) {
        return TipoConsulta.fromDescricao(nomeConsulta);
    }

    public void validarHorario(LocalDateTime horario, TipoConsulta tipoConsulta) {

        List<Consulta> consultasAgendadas = consultaRepository.findByStatus(StatusConsulta.AGENDADA);

        LocalDateTime inicioNovo = horario;
        LocalDateTime fimNovo = horario.plusMinutes(tipoConsulta.getDuracaoConsulta());

        for (Consulta consultaExistente : consultasAgendadas) {

            LocalDateTime inicioExistente = consultaExistente.getHorario();


            if (!inicioNovo.toLocalDate().equals(inicioExistente.toLocalDate())) {
                continue;
            }

            if (!tipoConsulta.getDescricao().equalsIgnoreCase(
                    consultaExistente.getEspecialidadeMedico())) {
                continue;
            }

            TipoConsulta tipoExistente = TipoConsulta.fromDescricao(
                    consultaExistente.getEspecialidadeMedico().trim()
            );

            LocalDateTime fimExistente = inicioExistente.plusMinutes(tipoExistente.getDuracaoConsulta());

            boolean haConflito = inicioNovo.isBefore(fimExistente) && fimNovo.isAfter(inicioExistente);

            if (haConflito) {
                throw new ConflictException(
                        String.format("Horário indisponível. O médico %s já possui consulta agendada de %s às %s",
                                tipoExistente.getDescricao(),
                                inicioExistente.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                                fimExistente.format(DateTimeFormatter.ofPattern("HH:mm"))
                        )
                );
            }
        }
    }
}
