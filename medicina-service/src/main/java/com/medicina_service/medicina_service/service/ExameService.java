package com.medicina_service.medicina_service.service;

import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoRequestDTO;
import com.medicina_service.medicina_service.dto.confirmacaodto.ConfirmacaoDeCriacaoResponseDTO;
import com.medicina_service.medicina_service.dto.modeldto.ExameDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoRequestDTO;
import com.medicina_service.medicina_service.dto.procedimentodto.request_responsedto.ProcedimentoResponseDTO;
import com.medicina_service.medicina_service.exception.custom.BadRequestException;
import com.medicina_service.medicina_service.exception.custom.ConflictException;
import com.medicina_service.medicina_service.exception.custom.ResourceNotFoundException;
import com.medicina_service.medicina_service.mapper.ExameMapper;
import com.medicina_service.medicina_service.mapper.ProcedimentoMapper;
import com.medicina_service.medicina_service.model.Exame;
import com.medicina_service.medicina_service.model.StatusAtendimento;
import com.medicina_service.medicina_service.model.TipoProcedimento;
import com.medicina_service.medicina_service.repository.ExameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExameService {

    private final ExameRepository exameRepository;
    private final ProcedimentoMapper procedimentoMapper;
    private final ExameMapper exameMapper;

    @Transactional
    public void processarProcedimentoDaClinica(ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Processando procedimento da clínica - CPF: {}, Tipo: {} ",
                procedimentoRequestDTO.getCpfPaciente(), procedimentoRequestDTO.getNomeExame());

        TipoProcedimento tipo;
        try {
            tipo = TipoProcedimento.fromDescricao(procedimentoRequestDTO.getNomeExame());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Tipo de exame não atendido pela rede: " + procedimentoRequestDTO.getNomeExame());
        }

        validarHorario(procedimentoRequestDTO.getHorarioProcedimento(),
                procedimentoRequestDTO.getPrioridade(),
                TipoProcedimento.fromDescricao(procedimentoRequestDTO.getNomeExame().trim()));

        Exame exame = Exame.builder()
                .agendamentoId(procedimentoRequestDTO.getAgendamentoId())
                .horarioExame(procedimentoRequestDTO.getHorarioProcedimento())
                .cpfPaciente(procedimentoRequestDTO.getCpfPaciente())
                .nomeExame(procedimentoRequestDTO.getNomeExame())
                .prioridade(procedimentoRequestDTO.getPrioridade())
                .statusAtendimento(StatusAtendimento.AGENDADO)
                .build();

        exameRepository.save(exame);
        log.info("Procedimento criado com ID: {}", exame.getId());
    }

    @Transactional
    public ProcedimentoResponseDTO criarExame(ProcedimentoRequestDTO procedimentoRequestDTO) {
        log.info("Criando procedimento: {} para o CPF: {}", procedimentoRequestDTO.getNomeProcedimento(), procedimentoRequestDTO.getCpfPaciente());

        TipoProcedimento tipo;
        try {
            tipo = TipoProcedimento.fromDescricao(procedimentoRequestDTO.getNomeExame());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Tipo de exame não atendido pela rede: " + procedimentoRequestDTO.getNomeExame());
        }

        validarHorario(procedimentoRequestDTO.getHorarioProcedimento(),
                procedimentoRequestDTO.getPrioridade(),
                TipoProcedimento.fromDescricao(procedimentoRequestDTO.getNomeExame().trim()));

        Exame exame = exameRepository.save(exameMapper.toExameEntity(procedimentoRequestDTO));

        return exameMapper.toExameDTO(exame);

    }

    public TipoProcedimento validarProcedimento(String nomeExame) {
        return TipoProcedimento.fromDescricao(nomeExame);
    }

    @Transactional
    public ConfirmacaoDeCriacaoResponseDTO marcarHorario(ConfirmacaoDeCriacaoRequestDTO confirmacaoDeCriacaoRequestDTO) {
        log.info("Confirmando horário de agendamento para o exame de id: {} ", confirmacaoDeCriacaoRequestDTO.getProcedimentoId());

        Exame exame = exameRepository
                .findById(confirmacaoDeCriacaoRequestDTO.getProcedimentoId())
                .orElseThrow(() -> {
                    log.error("Exame não encontrado pelo id: {} ", confirmacaoDeCriacaoRequestDTO.getProcedimentoId());
                    return new ResourceNotFoundException("Exame não encontrado pelo Id fornecido!");
                });


        validarHorario(confirmacaoDeCriacaoRequestDTO.getHorario(), exame.getPrioridade(),TipoProcedimento.fromDescricao(exame.getNomeExame().trim()));

        exame.setHorarioExame(confirmacaoDeCriacaoRequestDTO.getHorario());
        exame.setStatusAtendimento(StatusAtendimento.AGENDADO);

        Exame exameSalvo = exameRepository.save(exame);

        log.info("Horário agendado com sucesso!");

        return exameMapper.toConfirmacaoDeHorarioExame(exameSalvo);
    }

    @Cacheable(value = "exames", key = "#cpf")
    public List<ExameDTO> listarPorCpf(String cpf) {
        log.info("Listando exames do CPF: {} ", cpf);
        List<Exame> exame = exameRepository.findByCpfPaciente(cpf)
                .stream().toList();
        return exame.stream()
                .map(exames -> {
                    ExameDTO dto = exameMapper.toDTO(exames);
                    return dto;
                }).toList();
    }

    @Cacheable(value = "exames")
    public List<ExameDTO> listarExames() {
        log.info("Listando todos os exames");
        List<Exame> exame = exameRepository.findAll();
        return exame.stream()
                .map(exames -> {
                    ExameDTO dto = exameMapper.toDTO(exames);
                    return dto;
                }).toList();
    }

    @Cacheable(value = "exames", key = "'aguardando'")
    public List<ExameDTO> listarAguardandoAgendamento() {
        log.info("Listando exames aguardando agendamento");
        List<Exame> exame = exameRepository.findByStatusAtendimento(StatusAtendimento.AGUARDANDO_AGENDAMENTO).stream().toList();
        return exame.stream()
                .map(exames -> {
                    ExameDTO dto = exameMapper.toDTO(exames);
                    return dto;
                }).toList();
    }

    public void encerrarExame(Long id) {
        log.info("Encerrando procedimento de id: {}", id);
        Exame exame = exameRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Procedimento não encontrado pelo Id fornecido: {} ", id);
                    return new RuntimeException("Não encontrado pelo Id fornecido");
                });
        exame.setStatusAtendimento(StatusAtendimento.REALIZADO);
        exameRepository.save(exame);
    }

    public void deletarExame(Long id) {
        log.info("Deletando exame de id: {} ", id);
        Exame exame = exameRepository.findById(id)
                .or(() -> exameRepository.findByAgendamentoId(id))
                .orElseThrow(()-> new RuntimeException("Exame não encontrado pelo id fornecido"));
        exame.setStatusAtendimento(StatusAtendimento.CANCELADO);
        exameRepository.save(exame);
    }


    private void validarHorario(LocalDateTime horario, String prioridade, TipoProcedimento tipoProcedimento) {

        boolean isEmergencial = "emergencial".equalsIgnoreCase(prioridade);

        List<Exame> examesAgendados = exameRepository.findByStatusAtendimento(
                StatusAtendimento.AGENDADO);

        LocalDateTime inicioNovo = horario;
        LocalDateTime fimNovo = horario.plusMinutes(tipoProcedimento.getDuracaoProcedimento());

        for (Exame exameExistente : examesAgendados) {

            TipoProcedimento tipoExistente = TipoProcedimento.fromDescricao(
                    exameExistente.getNomeExame().trim());

            LocalDateTime inicioExistente = exameExistente.getHorarioExame();
            LocalDateTime fimExistente = inicioExistente.plusMinutes(tipoExistente.getDuracaoProcedimento());

            boolean haConflito =
                    (inicioNovo.isBefore(fimExistente) && inicioNovo.isAfter(inicioExistente)) ||
                            (fimNovo.isAfter(inicioExistente) && fimNovo.isBefore(fimExistente)) ||
                            (inicioNovo.isBefore(inicioExistente) && fimNovo.isAfter(fimExistente)) ||
                            inicioNovo.equals(inicioExistente);

            if (haConflito) {
                boolean existenteEmergencial = "emergencial".equalsIgnoreCase(
                        exameExistente.getPrioridade());

                if (isEmergencial && existenteEmergencial) {
                    throw new ConflictException(
                            "Já existe exame emergencial agendado neste horário");

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
}


