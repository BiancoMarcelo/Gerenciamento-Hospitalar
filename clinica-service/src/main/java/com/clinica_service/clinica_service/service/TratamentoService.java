package com.clinica_service.clinica_service.service;

import com.clinica_service.clinica_service.dto.tratamentodto.TratamentoDTO;
import com.clinica_service.clinica_service.exception.custom.ResourceNotFoundException;
import com.clinica_service.clinica_service.mapper.TratamentoMapper;
import com.clinica_service.clinica_service.model.Tratamento;
import com.clinica_service.clinica_service.repository.TratamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TratamentoService {

    private final TratamentoRepository tratamentoRepository;
    private final TratamentoMapper tratamentoMapper;

    @Transactional
    public TratamentoDTO criarTratamento(TratamentoDTO tratamentoDTO) {
        log.info("Criando novo tratamento: {} ", tratamentoDTO.getDescricaoTratamento());

        Tratamento tratamento = tratamentoRepository.save(tratamentoMapper.toEntity(tratamentoDTO));

        log.info("Tratamendo criado com id: {} ", tratamento.getId());
        return tratamentoMapper.toDTO(tratamento);
    }

    @Cacheable(value = "tratamentos")
    public List<TratamentoDTO> listarTratamentos() {
        log.info("Listando todos os tratamentos disponíveis");

        return tratamentoRepository.findAll()
                .stream()
                .map(tratamentoMapper::toDTO)
                .toList();
    }

    @Cacheable(value = "consultas", key = "#id")
    public TratamentoDTO buscarTratamento(Long id) {
        log.info("Buscando tratamento de id: {} ", id);

        return tratamentoRepository.findById(id)
                .map(tratamentoMapper::toDTO)
                .orElseThrow(()-> {
                    log.info("Tratamento não encontrado pelo id: {} ", id);
                    return new ResourceNotFoundException("Tratamento não encontrado!");
                });
    }

    @Transactional
    public TratamentoDTO atualizarTratamento (Long id, TratamentoDTO tratamentoDTO) {
        buscarTratamento(id);

        Tratamento tratamento = tratamentoMapper.toEntity(tratamentoDTO);
        tratamento.setId(id);

        Tratamento tratamentoSalvo = tratamentoRepository.save(tratamento);

        return tratamentoMapper.toDTO(tratamento);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando tratamento de id: {} ", id);
        if (!tratamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tratamento não encontrado pelo id!");
        }

        tratamentoRepository.deleteById(id);
        log.info("Tratamento deletado com sucesso!");
    }


}
