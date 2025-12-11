package com.clinica_service.clinica_service.repository;

import com.clinica_service.clinica_service.model.Sintoma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SintomaRepository extends JpaRepository<Sintoma, Long> {

    Optional<Sintoma> findByDescricao(String descricao);

    List<Sintoma> findByPrioridade(Integer prioridade);

    List<Sintoma> findByDescricaoIn(List<String> descricoes);
}
