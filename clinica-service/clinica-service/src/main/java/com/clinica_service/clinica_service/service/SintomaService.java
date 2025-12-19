package com.clinica_service.clinica_service.service;

import com.clinica_service.clinica_service.dto.sintomadto.SintomaDTO;
import com.clinica_service.clinica_service.exception.custom.ResourceNotFoundException;
import com.clinica_service.clinica_service.mapper.SintomaMapper;
import com.clinica_service.clinica_service.model.Sintoma;
import com.clinica_service.clinica_service.model.Tratamento;
import com.clinica_service.clinica_service.repository.SintomaRepository;
import com.clinica_service.clinica_service.repository.TratamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SintomaService {

    private final SintomaRepository sintomaRepository;
    private final TratamentoRepository tratamentoRepository;
    private final SintomaMapper sintomaMapper;

    @Transactional
    public SintomaDTO criarSintoma(SintomaDTO sintomaDTO) {
        log.info("Criando novo sintoma: {}", sintomaDTO.getDescricao());

//        if (sintomaRepository.existsByDescricaoIgnoreCase(sintomaDTO.getDescricao())){
//            throw new RuntimeException("Sintoma já cadastrado no banco de dados");
//        }

        Sintoma sintomaSalvo = sintomaRepository.save(sintomaMapper.toEntity(sintomaDTO));

        log.info("Sintoma criado com id: {} ", sintomaSalvo.getId());
        return sintomaMapper.toDTO(sintomaSalvo);
    }

    public SintomaDTO buscarPorId(Long id) {
        log.info("Buscando sintoma por ID: {}", id);

        Sintoma sintoma = sintomaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Sintoma não encontrado pelo ID: {}", id);
                    return new ResourceNotFoundException("Sintoma não encontrado");
                });

        return sintomaMapper.toDTO(sintoma);
    }

    public SintomaDTO buscarPorDescricao(String descricao) {
        log.info("Buscando sintoma por descrição: {}", descricao);

        Sintoma sintoma = sintomaRepository.findByDescricaoIgnoreCase(descricao.toLowerCase())
                .orElseThrow(() -> {
                    log.error("Sintoma não encontrado: {}", descricao);
                    return new ResourceNotFoundException("Sintoma não encontrado");
                });

        return sintomaMapper.toDTO(sintoma);
    }

    public List<SintomaDTO> listarTodos() {
        log.info("Listando todos os sintomas");

        return sintomaRepository.findAll()
                .stream()
                .map(sintomaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SintomaDTO> listarPorPrioridade(Integer prioridade) {
        log.info("Listando sintomas com prioridade: {}", prioridade);

        return sintomaRepository.findByPrioridade(prioridade)
                .stream()
                .map(sintomaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SintomaDTO atualizar(Long id, SintomaDTO sintomaDTO) {
        log.info("Atualizando sintoma ID: {}", id);

        Sintoma sintoma = sintomaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sintoma não encontrado"));

        sintoma.setDescricao(sintomaDTO.getDescricao().toLowerCase());
        sintoma.setPrioridade(sintomaDTO.getPrioridade());

        Sintoma sintomaAtualizado = sintomaRepository.save(sintoma);
        log.info("Sintoma atualizado com sucesso");

        return sintomaMapper.toDTO(sintomaAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando sintoma ID: {}", id);

        if (!sintomaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sintoma não encontrado");
        }

        sintomaRepository.deleteById(id);
        log.info("Sintoma deletado com sucesso");

    }

    @Transactional
    public SintomaDTO associarTratamentos(Long sintomaId, List<Long> tratamentosIds) {
        log.info("Associando tratamentos ao sintoma ID: {}", sintomaId);

        Sintoma sintoma = sintomaRepository.findById(sintomaId)
                .orElseThrow(() -> new RuntimeException("Sintoma não encontrado"));

        List<Tratamento> tratamentos = tratamentoRepository.findAllById(tratamentosIds);

        sintoma.setTratamentosSugeridos(tratamentos);
        sintomaRepository.save(sintoma);

        log.info("Tratamentos associados com sucesso");
        return sintomaMapper.toDTO(sintoma);
    }
}
