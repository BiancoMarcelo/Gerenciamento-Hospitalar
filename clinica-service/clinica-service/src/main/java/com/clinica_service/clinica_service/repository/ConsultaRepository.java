package com.clinica_service.clinica_service.repository;

import com.clinica_service.clinica_service.model.Consulta;
import com.clinica_service.clinica_service.model.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
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


}
