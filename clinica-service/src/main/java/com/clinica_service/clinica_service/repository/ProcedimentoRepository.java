package com.clinica_service.clinica_service.repository;

import com.clinica_service.clinica_service.model.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {

    List<Procedimento> findByAltaComplexidade(Boolean altaComplexidade);
}
