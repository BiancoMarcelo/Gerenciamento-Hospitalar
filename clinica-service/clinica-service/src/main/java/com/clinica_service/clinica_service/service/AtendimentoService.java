package com.clinica_service.clinica_service.service;

import com.clinica_service.clinica_service.dto.consultadto.atenderconsulta.AtenderConsultaRequestDTO;
import com.clinica_service.clinica_service.dto.consultadto.atenderconsulta.AtenderConsultaResponseDTO;
import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoDTO;
import com.clinica_service.clinica_service.mapper.ProcedimentoMapper;
import com.clinica_service.clinica_service.messaging.ProcedimentoPublisher;
import com.clinica_service.clinica_service.model.*;
import com.clinica_service.clinica_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final ConsultaService consultaService;
    private final SintomaRepository sintomaRepository;
    private final DoencaRepository doencaRepository;
    private final ProcedimentoMapper procedimentoMapper;
    private final ProcedimentoPublisher procedimentoPublisher;

    @Transactional
    public AtenderConsultaResponseDTO atenderConsulta (AtenderConsultaRequestDTO atenderConsultaRequestDTO) {
        log.info("Iniciando atendimento pelo CPF: {} ", atenderConsultaRequestDTO.getCpfPaciente());

        Consulta consulta = buscarConsulta(atenderConsultaRequestDTO);

        if (consulta.getStatus() == StatusConsulta.ATENDIDA) {
            throw new RuntimeException("Essa consulta já foi atendida!");
        }

        List<Sintoma> sintomas = processarSintomas(atenderConsultaRequestDTO.getSintomas());

        List<Doenca> possiveisDoencas = sugerirDoencas(atenderConsultaRequestDTO.getSintomas());

        List<String> tratamentosSugeridos = coletarTratamentos(sintomas);

        List<ProcedimentoDTO> procedimentosNecessarios = verificarProcedimentos(
                possiveisDoencas,
                atenderConsultaRequestDTO.getCpfPaciente(),
                sintomas
        );

        Atendimento atendimento = Atendimento.builder()
                .consulta(consulta)
                .sintomas(sintomas)
                .possiveisDoencas(possiveisDoencas)
                .build();

        atendimentoRepository.save(atendimento);
        consultaService.atualizarStatusConsulta(consulta.getId(), StatusConsulta.ATENDIDA);

        if (!procedimentosNecessarios.isEmpty()) {
            enviarProcedimentosParaCentroCirurgico(procedimentosNecessarios);
        }

        log.info("Atendimento de Id: {} finalizado.", atendimento.getId());

        return AtenderConsultaResponseDTO.builder()
                .atendimentoId(atendimento.getId())
                .possiveisDoencas(possiveisDoencas.stream()
                        .map(Doenca::getNomeDoenca)
                        .collect(Collectors.toList()))
                .tratamentosSugeridos(tratamentosSugeridos)
                .procedimentosNecessarios(procedimentosNecessarios)
                .mensagem("Atendimento realizado com sucesso")
                .build();

    }

    private Consulta buscarConsulta(AtenderConsultaRequestDTO atenderConsultaRequestDTO) {
        if (atenderConsultaRequestDTO.getHorario() != null) {
            log.info("Buscando consulta por CPF: {} e horário: {} ", atenderConsultaRequestDTO.getCpfPaciente(), atenderConsultaRequestDTO.getHorario());
            return consultaService.buscarPorCpfEHorario(atenderConsultaRequestDTO.getCpfPaciente(), atenderConsultaRequestDTO.getHorario());
        }

        if (atenderConsultaRequestDTO.getCodigoConsulta() != null) {
            log.info("Buscando consulta por código: {} ", atenderConsultaRequestDTO.getCodigoConsulta());
            Consulta consulta = consultaService.buscarPorId(atenderConsultaRequestDTO.getCodigoConsulta());

            if (!consulta.getCpfPaciente().equals(atenderConsultaRequestDTO.getCpfPaciente())) {
                throw new RuntimeException("Consulta não pertence ao CPF informado");
            }

            return consulta;
        }

        throw new RuntimeException("Necessário informar ou horário ou código da consulta");
    }

    private List<Sintoma> processarSintomas(List<String>descricoesSintomas) {
        log.info("Processando {} sintomas", descricoesSintomas.size());

        List<Sintoma> sintomas = new ArrayList<>();

        for (String descricao : descricoesSintomas) {
            Sintoma sintoma = sintomaRepository.findByDescricao(descricao.toLowerCase())
                    .orElseGet(()-> {
                        log.warn("Sintoma '{}'não encontrado no banco de dados. Criando com prioridade padrão.", descricao);
                        Sintoma novoSintoma = Sintoma.builder()
                                .descricao(descricao.toLowerCase())
                                .prioridade(2)
                                .build();
                        return sintomaRepository.save(novoSintoma);
                    });
            sintomas.add(sintoma);
        }

        return sintomas;
    }

    private List<Doenca> sugerirDoencas(List<String> descricoesSintomas) {
        log.info("Sugerindo doenças baseadas nos sintomas");

        List<String> sintomasInformados = descricoesSintomas.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        return doencaRepository.findBysintomasDescricaoIn(sintomasInformados);
    }

    private List<String> coletarTratamentos(List<Sintoma> sintomas) {
        log.info("Coletando tratamentos sugeridos");

        return sintomas.stream()
                .flatMap(sintoma -> sintoma.getTratamentosSugeridos().stream())
                        .map(Tratamento::getDescricaoTratamento)
                        .distinct()
                        .collect(Collectors.toList());
    }

    private List<ProcedimentoDTO> verificarProcedimentos(
            List<Doenca> doencas,
            String cpfPaciente,
            List<Sintoma> sintomas) {
        log.info("Verificando necessidade de procedimentos de alta complexidade");

        List<ProcedimentoDTO> procedimentos = new ArrayList<>();

        Integer prioridadeMaxima = sintomas.stream()
                .map(Sintoma::getPrioridade)
                .max(Integer::compareTo)
                .orElse(2);

        for (Doenca doenca : doencas) {
            if (doenca.getProcedimentosNecessarios() != null ) {
                for (Procedimento procedimento : doenca.getProcedimentosNecessarios()) {
                    if (Boolean.TRUE.equals(procedimento.getAltaComplexidade())) {
                        ProcedimentoDTO dto = procedimentoMapper.toDTO(
                                procedimento,
                                cpfPaciente,
                                prioridadeMaxima
                        );
                        procedimentos.add(dto);
                    }
                }
            }
        }

        return procedimentos;
    }

    private void enviarProcedimentosParaCentroCirurgico(List<ProcedimentoDTO> procedimentoDTO) {
        log.info("Enviando {} procedimentos para Centro Cirúrgico via rabbitMQ", procedimentoDTO.size());

        for (ProcedimentoDTO proc : procedimentoDTO) {
            procedimentoPublisher.enviarProcedimento(proc);

        }
    }
}
