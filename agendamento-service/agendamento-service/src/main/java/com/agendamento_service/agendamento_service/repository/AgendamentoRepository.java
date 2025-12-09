package com.agendamento_service.agendamento_service.repository;

import com.agendamento_service.agendamento_service.model.Agendamento;
import com.agendamento_service.agendamento_service.model.TipoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {



    boolean existsByPaciente_CpfAndHorario(
            String cpf,
            LocalDateTime horario
    );

    boolean existsByEspecialidadeAndHorarioAndTipoAgendamento(
            String especialidade,
            LocalDateTime horario,
            TipoAgendamento tipoAgendamento
    );

    boolean existsByTipoExameAndHorarioAndTipoAgendamento(
            String tipoExame,
            LocalDateTime horario,
            TipoAgendamento tipoAgendamento
    );

    List<Agendamento> findByPaciente_Cpf(String cpf);

}
