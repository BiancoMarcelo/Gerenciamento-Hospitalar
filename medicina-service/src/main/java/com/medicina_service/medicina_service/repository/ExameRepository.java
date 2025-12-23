package com.medicina_service.medicina_service.repository;

import com.medicina_service.medicina_service.model.Exame;
import com.medicina_service.medicina_service.model.StatusAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {

    List<Exame> findByCpfPaciente(String cpf);

    boolean existsByCpfPaciente(String cpf);

    boolean existsByHorarioExame(LocalDateTime horario);

    List<Exame> findByStatusAtendimento(StatusAtendimento status);

    List<Exame> findByHorarioExameAndPrioridade(
            LocalDateTime horario,
            String prioridade
    );

    Optional<Exame> findByAgendamentoId(Long id);

    boolean existsByHorarioExameAndPrioridadeNot(LocalDateTime horario, String prioridade);


}
