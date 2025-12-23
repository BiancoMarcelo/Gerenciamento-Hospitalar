package com.medicina_service.medicina_service.service;

import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoRequestDTO;
import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoResponseDTO;
import com.medicina_service.medicina_service.dto.modeldto.ExameDTO;
import com.medicina_service.medicina_service.dto.modeldto.ProcedimentoDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoResponseDTO;
import com.medicina_service.medicina_service.exception.custom.BadRequestException;
import com.medicina_service.medicina_service.exception.custom.ConflictException;
import com.medicina_service.medicina_service.exception.custom.ResourceNotFoundException;
import com.medicina_service.medicina_service.exception.custom.UnauthorizedException;
import com.medicina_service.medicina_service.mapper.ProcedimentoMapper;
import com.medicina_service.medicina_service.model.Exame;
import com.medicina_service.medicina_service.model.Procedimento;
import com.medicina_service.medicina_service.model.StatusAtendimento;
import com.medicina_service.medicina_service.model.TipoProcedimento;
import com.medicina_service.medicina_service.repository.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcedimentoService {

    private final ProcedimentoRepository procedimentoRepository;
    private final ProcedimentoMapper procedimentoMapper;

    @Transactional
    public void processarProcedimentoDaClinica(ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Processando procedimento da clínica - CPF: {}, Tipo: {} ",
                procedimentoRequestDTO.getCpfPaciente(), procedimentoRequestDTO.getNomeProcedimento());

        TipoProcedimento tipo;
        try {
            tipo = TipoProcedimento.fromDescricao(procedimentoRequestDTO.getNomeProcedimento());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Tipo de procedimento não atendido pela rede: " + procedimentoRequestDTO.getNomeProcedimento());
        }

        validarHorario(procedimentoRequestDTO.getHorarioProcedimento(),
                procedimentoRequestDTO.getPrioridade(),
                TipoProcedimento.fromDescricao(procedimentoRequestDTO.getNomeProcedimento().trim()));

        Procedimento procedimentoCirurgico = Procedimento.builder()
                .agendamentoId(procedimentoRequestDTO.getAgendamentoId())
                .cpfPaciente(procedimentoRequestDTO.getCpfPaciente())
                .nomeProcedimento(procedimentoRequestDTO.getNomeProcedimento())
                .prioridade(procedimentoRequestDTO.getPrioridade())
                .statusAtendimento(StatusAtendimento.AGUARDANDO_AGENDAMENTO)
                .build();

        procedimentoRepository.save(procedimentoCirurgico);
        log.info("Procedimento criado com ID: {}", procedimentoCirurgico.getId());
    }

    @Transactional
    public ProcedimentoResponseDTO criarProcedimento (ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Criando procedimento: {} para o CPF: {}, com prioridade: {}",
                procedimentoRequestDTO.getNomeProcedimento(), procedimentoRequestDTO.getCpfPaciente(),
                procedimentoRequestDTO.getPrioridade());

        if (procedimentoRequestDTO.getCpfPaciente() == null ||
                procedimentoRequestDTO.getNomeProcedimento() == null ||
                procedimentoRequestDTO.getPrioridade() == null) {
            throw new BadRequestException("CPF, código do exame e prioridade são obrigatórios!");
        }

        TipoProcedimento tipo;

        try {
            tipo = TipoProcedimento.fromDescricao(procedimentoRequestDTO.getNomeProcedimento());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Tipo de procedimento não atendido pela rede: " + procedimentoRequestDTO.getNomeProcedimento());
        }

        Procedimento procedimentoCirurgico = procedimentoRepository.save(procedimentoMapper.toProcedimentoCirurgicoEntity(procedimentoRequestDTO));

        return procedimentoMapper.toProcedimentoCirurgicoDTO(procedimentoCirurgico);

    }

    @Transactional
    public ConfirmacaoDeCriacaoResponseDTO marcarHorario(ConfirmacaoDeCriacaoRequestDTO confirmacaoDeCriacaoRequestDTO) {
        log.info("Confirmando horário de agendamento para o procedimento de id: {} ", confirmacaoDeCriacaoRequestDTO.getProcedimentoId());

        Procedimento procedimento = procedimentoRepository
                .findById(confirmacaoDeCriacaoRequestDTO.getProcedimentoId())
                .orElseThrow(() -> {
                    log.error("Procedimento não encontrado pelo id: {} ",confirmacaoDeCriacaoRequestDTO.getProcedimentoId());
                    return new ResourceNotFoundException("Procedimento não encontrado pelo Id fornecido!");
                });

        validarHorario(confirmacaoDeCriacaoRequestDTO.getHorario(),
                procedimento.getPrioridade(),
                TipoProcedimento.fromDescricao(procedimento.getNomeProcedimento().trim()));

        procedimento.setHorarioProcedimento(confirmacaoDeCriacaoRequestDTO.getHorario());
        procedimento.setStatusAtendimento(StatusAtendimento.AGENDADO);

        Procedimento procedimentoSalvo = procedimentoRepository.save(procedimento);

        sobrescreverHorario(confirmacaoDeCriacaoRequestDTO.getHorario());

        log.info("Horário agendado com sucesso!");

        return procedimentoMapper.toProcedimentoCirurgicoConfirmarHorarioDTO(procedimentoSalvo);
    }

    public List<ProcedimentoDTO> listarPorCpf(String cpf) {
        log.info("Listando procedimentos do CPF: {} ", cpf);
        List<Procedimento> procedimentos = procedimentoRepository.findByCpfPaciente(cpf)
                .stream().toList();
        return procedimentos.stream()
                .map(procedimento -> {
                    ProcedimentoDTO dto = procedimentoMapper.toDTO(procedimento);
                    return dto;
                }).toList();
    }

    public List<ProcedimentoDTO> listarProcedimentos() {
        log.info("Listando todos os procedimentos");
        List<Procedimento> procedimentos = procedimentoRepository.findAll();
        return procedimentos.stream()
                .map(procedimento -> {
                    ProcedimentoDTO dto = procedimentoMapper.toDTO(procedimento);
                    return dto;
                }).toList();
    }

    public List<ProcedimentoDTO> listarAguardandoAgendamento() {
        log.info("Listando procedimentos aguardando agendamento");
        List<Procedimento> procedimento = procedimentoRepository.findByStatusAtendimento(StatusAtendimento.AGUARDANDO_AGENDAMENTO).stream().toList();
        return procedimento.stream()
                .map(procedimentos -> {
                    ProcedimentoDTO dto = procedimentoMapper.toDTO(procedimentos);
                    return dto;
                }).toList();
    }

    public void encerrarProcedimento(Long id) {
        log.info("Encerrando procedimento de id: {}", id);
        Procedimento procedimento = procedimentoRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Procedimento não encontrado pelo Id fornecido: {} ", id);
                    return new RuntimeException("Não encontrado pelo Id fornecido");
                        });
        procedimento.setStatusAtendimento(StatusAtendimento.REALIZADO);
        procedimentoRepository.save(procedimento);
    }

    public void cancelarProcedimento(Long id) {
        log.info("Encerrando procedimento de id: {}", id);
        Procedimento procedimento = procedimentoRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Procedimento não encontrado pelo Id fornecido: {} ", id);
                    return new RuntimeException("Não encontrado pelo Id fornecido");
                });
        procedimento.setStatusAtendimento(StatusAtendimento.CANCELADO);
        procedimentoRepository.save(procedimento);
    }

    public void deletarProcedimento(Long id) {
        log.info("Deletando procedimento de id: {}", id);
        Procedimento procedimento = procedimentoRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Procedimento não encontrado pelo Id fornecido: {} ", id);
                    return new RuntimeException("Não encontrado pelo Id fornecido");
                });
        procedimentoRepository.deleteById(id);
    }


    private void validarHorario(LocalDateTime horario, String prioridade, TipoProcedimento tipoProcedimento) {

        boolean isEmergencial = "emergencial".equalsIgnoreCase(prioridade);

        List<Procedimento> procedimentosAgendados = procedimentoRepository.findByStatusAtendimento(
                StatusAtendimento.AGENDADO);

        LocalDateTime inicioNovo = horario;
        LocalDateTime fimNovo = horario.plusMinutes(tipoProcedimento.getDuracaoProcedimento());

        for (Procedimento procedimentoExistente : procedimentosAgendados) {

            TipoProcedimento tipoExistente = TipoProcedimento.fromDescricao(
                    procedimentoExistente.getNomeProcedimento().trim());

            LocalDateTime inicioExistente = procedimentoExistente.getHorarioProcedimento();
            LocalDateTime fimExistente = inicioExistente.plusMinutes(tipoExistente.getDuracaoProcedimento());

            boolean haConflito =
                    (inicioNovo.isBefore(fimExistente) && inicioNovo.isAfter(inicioExistente)) ||
                            (fimNovo.isAfter(inicioExistente) && fimNovo.isBefore(fimExistente)) ||
                            (inicioNovo.isBefore(inicioExistente) && fimNovo.isAfter(fimExistente)) ||
                            inicioNovo.equals(inicioExistente);

            if (haConflito) {
                boolean existenteEmergencial = "emergencial".equalsIgnoreCase(
                        procedimentoExistente.getPrioridade());

                if (isEmergencial && existenteEmergencial) {
                    throw new ConflictException(
                            "Já existe procedimento emergencial agendado neste horário");

                } else if (!isEmergencial && !existenteEmergencial) {
                    throw new ConflictException(
                            String.format("Horário indisponível. Já existe %s agendado de %s até %s",
                                    tipoExistente.getDescricao(),
                                    inicioExistente.format(DateTimeFormatter.ofPattern("HH:mm")),
                                    fimExistente.format(DateTimeFormatter.ofPattern("HH:mm"))));

                }
            }
        }
    }

    @Transactional
    public void sobrescreverHorario(LocalDateTime horario) {

        List<Procedimento> procedimentosNoHorario = procedimentoRepository.findAllByHorarioProcedimento(horario);

        if (procedimentosNoHorario.size() > 1) {
            log.info("Quantidade de horários simultaneos encontrados: {} ", procedimentosNoHorario.size());

            procedimentosNoHorario.forEach(proc -> {
                if (!proc.getPrioridade().equalsIgnoreCase("emergencial")) {
                    log.info("Deletando procedimento de id: {} ", proc.getId());
                    cancelarProcedimento(proc.getId());
                }
            });
        }

    }


}
