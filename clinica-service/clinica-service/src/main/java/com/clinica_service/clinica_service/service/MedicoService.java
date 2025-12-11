package com.clinica_service.clinica_service.service;

import com.clinica_service.clinica_service.dto.medicodto.MedicoDTO;
import com.clinica_service.clinica_service.mapper.MedicoMapper;
import com.clinica_service.clinica_service.model.Medico;
import com.clinica_service.clinica_service.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    @Transactional
    public MedicoDTO criarNovoMedico(MedicoDTO medicoDTO) {
        log.info("Criando novo médico de nome: {} ", medicoDTO.getNomeMedico());

        Medico medicoSalvo = medicoRepository.save(medicoMapper.toEntity(medicoDTO));

        log.info("Medico criado com Id: {} ", medicoSalvo.getId());
        return medicoMapper.toDTO(medicoSalvo);
    }

    public MedicoDTO buscarPorId(Long id) {
        log.info("Buscando médico por id: {} ", id);

        return medicoMapper.toDTO(medicoRepository.findById(id)
                .orElseThrow(()-> {
            log.error("Medico não encontrado pelo Id fornecido: {} ", id);
            return new RuntimeException("Medico não encontrado pelo Id fornecido");
        }));
    }

    public MedicoDTO buscarMedicoPorNome(String nomeMedico) {
        log.info("Buscando médico de nome: {} ", nomeMedico);

        return medicoMapper.toDTO(medicoRepository.findByNomeMedico(nomeMedico)
                .orElseThrow(()->{
                    log.error("Médico não encontrado pelo nome: {}", nomeMedico);
                    return new RuntimeException("Médico não encontrado pelo nome fornecido");
                }));
    }

    public List<MedicoDTO> listarTodosOsMedicos() {
        log.info("Listando todos os médicos");
        return medicoRepository.findAll()
                .stream()
                .map(medicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MedicoDTO> listarMedicoPorEspecialidade(String especialidade) {
        log.info("Listando todos os {}s", especialidade);

        return medicoRepository.findByEspecialidade(especialidade)
                .stream()
                .map(medicoMapper::toDTO)
                .collect((Collectors.toList()));
    }

    @Transactional
    public MedicoDTO atualizar(Long id, MedicoDTO medicoDTO) {
        log.info("Atualizando médico de Id: {} ", id);

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(()->
                    new RuntimeException("Médico não encontrado pelo Id fornecido"));

        medico.setNomeMedico(medico.getNomeMedico());
        medico.setEspecialidade(medico.getEspecialidade());

        Medico medicoAtualizado = medicoRepository.save(medico);
        log.info("Medico atualizado com sucesso");

        return medicoMapper.toDTO(medicoAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando médico de Id: {} ", id);

        if (!medicoRepository.existsById(id)) {
            throw new RuntimeException("Médico não encontrado");
        }

        medicoRepository.deleteById(id);
        log.info("Médico deletado com sucesso");
    }
}
