package com.medicina_service.medicina_service.repository;

import com.medicina_service.medicina_service.model.Procedimento;
import com.medicina_service.medicina_service.model.StatusAtendimento;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {

    List<Procedimento> findByCpfPaciente(String cpf);

    boolean existsByCpfPaciente(String cpf);

    boolean existsByHorarioProcedimentoAndPrioridadeNot(
            LocalDateTime horario,
            String prioridade
    );

    List<Procedimento> findAllByHorarioProcedimento(LocalDateTime horario);

    boolean existsByCpfPacienteAndHorarioProcedimento(String cpf, LocalDateTime horario);

    List<Procedimento> findByStatusAtendimento(StatusAtendimento status);

    List<Procedimento> findByHorarioProcedimentoAndPrioridade(
            LocalDateTime horario,
            String prioridade
    );

}
