package com.clinica_service.clinica_service.service;

import com.clinica_service.clinica_service.dto.procedimentodto.ProcedimentoDTO;
import com.clinica_service.clinica_service.mapper.ProcedimentoMapper;
import com.clinica_service.clinica_service.model.Procedimento;
import com.clinica_service.clinica_service.repository.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcedimentoService {

    private final ProcedimentoRepository procedimentoRepository;
    private final ProcedimentoMapper procedimentoMapper;

    @Transactional
    public Procedimento criar(String descricao, Boolean altaComplexidade) {
        log.info("Criando novo procedimento: {}", descricao);

        Procedimento procedimento = Procedimento.builder()
                .descricaoProcedimento(descricao)
                .altaComplexidade(altaComplexidade != null ? altaComplexidade : false)
                .build();

        Procedimento procedimentoSalvo = procedimentoRepository.save(procedimento);

        log.info("Procedimento criado com ID: {}", procedimentoSalvo.getId());
        return procedimentoSalvo;
    }

    public Procedimento buscarPorId(Long id) {
        log.info("Buscando procedimento por ID: {}", id);

        return procedimentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Procedimento não encontrado pelo ID: {}", id);
                    return new RuntimeException("Procedimento não encontrado");
                });
    }

    public List<Procedimento> listarTodos() {
        log.info("Listando todos os procedimentos");
        return procedimentoRepository.findAll();
    }

    public List<Procedimento> listarPorAltaComplexidade(Boolean altaComplexidade) {
        log.info("Listando procedimentos de alta complexidade: {}", altaComplexidade);
        return procedimentoRepository.findByAltaComplexidade(altaComplexidade);
    }

    @Transactional
    public Procedimento atualizar(Long id, String descricao, Boolean altaComplexidade) {
        log.info("Atualizando procedimento ID: {}", id);

        Procedimento procedimento = buscarPorId(id);

        procedimento.setDescricaoProcedimento(descricao);
        procedimento.setAltaComplexidade(altaComplexidade);

        Procedimento procedimentoAtualizado = procedimentoRepository.save(procedimento);
        log.info("Procedimento atualizado com sucesso");

        return procedimentoAtualizado;
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando procedimento ID: {}", id);

        if (!procedimentoRepository.existsById(id)) {
            throw new RuntimeException("Procedimento não encontrado");
        }

        procedimentoRepository.deleteById(id);
        log.info("Procedimento deletado com sucesso");
    }
}
