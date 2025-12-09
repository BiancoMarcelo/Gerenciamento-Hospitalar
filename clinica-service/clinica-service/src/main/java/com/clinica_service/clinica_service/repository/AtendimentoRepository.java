package com.clinica_service.clinica_service.repository;

import com.clinica_service.clinica_service.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    Optional<Atendimento> findByConsulta_Id(Long consultaId);

}
