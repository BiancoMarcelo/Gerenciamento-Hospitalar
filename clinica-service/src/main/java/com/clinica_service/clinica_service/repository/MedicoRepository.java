package com.clinica_service.clinica_service.repository;

import com.clinica_service.clinica_service.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    List<Medico> findByEspecialidade(String especialidade);

    Optional<Medico> findByNomeMedico(String nomeMedico);
}
