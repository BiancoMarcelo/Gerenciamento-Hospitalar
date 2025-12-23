package com.clinica_service.clinica_service.service;

import com.clinica_service.clinica_service.dto.doencadto.DoencaDTO;
import com.clinica_service.clinica_service.exception.custom.ResourceNotFoundException;
import com.clinica_service.clinica_service.mapper.DoencaMapper;
import com.clinica_service.clinica_service.model.Doenca;
import com.clinica_service.clinica_service.repository.DoencaRepository;
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
public class DoencaService {

    private final DoencaRepository doencaRepository;
    private final DoencaMapper doencaMapper;

    @Transactional
    public DoencaDTO criarDoenca(DoencaDTO doencaDTO) {
        log.info("Criando nova doença de nome: {} ", doencaDTO.getNomeDoenca());

        Doenca doencaSalva = doencaRepository.save(doencaMapper.toEntity(doencaDTO));

        log.info("Doenca {} criada com id: {}", doencaSalva.getNomeDoenca(), doencaSalva.getId());

        return doencaMapper.toDTO(doencaSalva);

    }

    @Cacheable(value = "doencas")
    public List<DoencaDTO> listarDoencas() {
        log.info("Listando todas as doenças");
        return doencaRepository.findAll().stream()
                .map(doencaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "doencas", key = "#id")
    public DoencaDTO listarDoencaPorId(Long id) {
        log.info("Buscando doença de id: {} ", id);
        return doencaRepository.findById(id)
                .map(doencaMapper::toDTO)
                .orElseThrow(()-> {
                    log.error("Doença não encontrada pelo Id: {} ", id);
                    return new ResourceNotFoundException("Doença não encontrada pelo ID informado");
                });
    }

    @Transactional
    public DoencaDTO atualizarDoenca(Long id, DoencaDTO doencaDTO) {
        log.info("Atualizando doença de id: {} ", id);
        listarDoencaPorId(id);

        Doenca doencaAtualizada = doencaMapper.toEntity(doencaDTO);
        doencaAtualizada.setId(id);

        Doenca doencaSalva = doencaRepository.save(doencaAtualizada);
        log.info("Doença atualizada com sucesso");

        return doencaMapper.toDTO(doencaSalva);

    }

    @Transactional
    public void deletarDoenca (Long id) {
        log.info("Deletando doença de id: {} ", id);

        if (!doencaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doença não encontrada");
        }

        doencaRepository.deleteById(id);
        log.info("Doença deletada do banco de dados");
    }


}
