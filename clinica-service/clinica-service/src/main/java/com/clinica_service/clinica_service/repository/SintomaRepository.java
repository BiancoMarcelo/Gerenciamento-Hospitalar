package com.clinica_service.clinica_service.repository;

import com.clinica_service.clinica_service.model.Sintoma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SintomaRepository extends JpaRepository<Sintoma, Long> {



    Optional<Sintoma> findByDescricaoIgnoreCase(String descricao);

    List<Sintoma> findByPrioridade(Integer prioridade);

    @Query("SELECT s FROM Sintoma s WHERE LOWER(s.descricao) IN :descricoes")
    List<Sintoma> findByDescricaoIn(@Param("descricoes") List<String> descricoes);

    boolean existsByDescricaoIgnoreCase(String descricao);
}
