package com.clinica_service.clinica_service.repository;

import com.clinica_service.clinica_service.model.Consulta;
import com.clinica_service.clinica_service.model.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Optional<Consulta> findByAgendamentoId(Long agendamentoId);

    Optional<Consulta> findByCpfPacienteAndHorario(String cpf, LocalDateTime horario);

    List<Consulta> findByCpfPaciente(String cpf);

    List<Consulta> findByStatus(StatusConsulta statusConsulta);

    boolean existsByEspecialidadeMedicoAndHorario(String especialidade, LocalDateTime horario);

    boolean existsByCpfPaciente(String cpf);

    @Query("SELECT c.especialidadeMedico FROM Consulta c WHERE c.agendamentoId = :id")
    String findEspecialidadeByAgendamentoId(@Param("id") Long id);

    @Query("SELECT c.horario FROM Consulta c WHERE c.agendamentoId = :id")
    LocalDateTime findHorarioByAgendamentoId(@Param("id") Long id);


}
