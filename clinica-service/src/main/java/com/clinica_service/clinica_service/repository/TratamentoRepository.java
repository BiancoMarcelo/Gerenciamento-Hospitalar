package com.clinica_service.clinica_service.repository;

import com.clinica_service.clinica_service.model.Tratamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TratamentoRepository extends JpaRepository<Tratamento, Long> {
}
