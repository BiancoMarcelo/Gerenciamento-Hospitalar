package com.clinica_service.clinica_service.repository;

import com.clinica_service.clinica_service.model.Doenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoencaRepository extends JpaRepository<Doenca, Long> {

    @Query("SELECT DISTINCT d FROM Doenca d JOIN d.sintomas s WHERE s.descricao IN :sintomas")
    List<Doenca> findBysintomasDescricaoIn(@Param("sintomas")List<String> sintomas);
}
