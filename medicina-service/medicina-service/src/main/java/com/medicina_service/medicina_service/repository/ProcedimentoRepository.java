package com.medicina_service.medicina_service.repository;

import com.medicina_service.medicina_service.model.Procedimento;
import com.medicina_service.medicina_service.model.StatusAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {

    List<Procedimento> findByCpfPaciente(String cpf);

    boolean existsByCpfPaciente(String cpf);

    boolean existsByHorarioProcedimentoAndPrioridadeNot(
            LocalDateTime horario,
            String prioridade
    );

    boolean existsByCpfPacienteAndHorarioProcedimento(String cpf, LocalDateTime horario);

    List<Procedimento> findByStatusAtendimento(StatusAtendimento status);

    List<Procedimento> findByHorarioProcedimentoAndPrioridade(
            LocalDateTime horario,
            String prioridade
    );

}
