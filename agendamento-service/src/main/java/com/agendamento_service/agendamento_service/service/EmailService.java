package com.agendamento_service.agendamento_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String from;

    @Value("${app.email.enabled:true}")
    private boolean enabled;

    @Async
    public void enviarEmailConsultaAgendada(String destinatario, String nomePaciente,
                                            String especialidade, String horario, String codigo) {
        if (!enabled) {
            log.info("Envio de email desabilitado");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(destinatario);
            message.setSubject("Consulta Agendada - Hospital Management");
            message.setText(String.format(
                    "Olá %s,\n\n" +
                            "Sua consulta foi agendada com sucesso!\n\n" +
                            "Detalhes:\n" +
                            "- Especialidade: %s\n" +
                            "- Data/Hora: %s\n" +
                            "- Código: %s\n\n" +
                            "Por favor, chegue com 15 minutos de antecedência.\n\n" +
                            "Atenciosamente,\n" +
                            "Hospital Management",
                    nomePaciente, especialidade, horario, codigo
            ));

            mailSender.send(message);
            log.info("Email enviado com sucesso para: {}", destinatario);

        } catch (Exception e) {
            log.error("Erro ao enviar email: {}", e.getMessage());
        }
    }

    @Async
    public void enviarEmailExameAgendado(String destinatario, String nomePaciente,
                                         String nomeExame, String horario, String codigo) {
        if (!enabled) {
            log.info("Envio de email desabilitado");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(destinatario);
            message.setSubject("Exame Agendado - Hospital Management");
            message.setText(String.format(
                    "Olá %s,\n\n" +
                            "Seu exame foi agendado com sucesso!\n\n" +
                            "Detalhes:\n" +
                            "- Exame: %s\n" +
                            "- Data/Hora: %s\n" +
                            "- Código: %s\n\n" +
                            "Importante: Compareça em jejum de 8 horas (se aplicável).\n\n" +
                            "Atenciosamente,\n" +
                            "Hospital Management",
                    nomePaciente, nomeExame, horario, codigo
            ));

            mailSender.send(message);
            log.info("Email enviado com sucesso para: {}", destinatario);

        } catch (Exception e) {
            log.error("Erro ao enviar email: {}", e.getMessage());
        }
    }

    @Async
    public void enviarEmailCriacaoUsuario(String destinatario, String nomePaciente) {
        if (!enabled) {
            log.info("Envio de email desabilitado");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(destinatario);
            message.setSubject("Criação de Usuário - Hospital Management");
            message.setText(String.format(
                    "Olá %s,\n\n" +
                            "Seu usuário foi criado com sucesso!\n\n" +
                            "Detalhes:\n" +
                            "- Nome do Paciente: %s\n" +
                            "- Email cadastrado: %s\n\n" +
                            "Atenciosamente,\n" +
                            "Hospital Management",
                    nomePaciente, nomePaciente, destinatario
            ));

            mailSender.send(message);
            log.info("Email enviado com sucesso para: {}", destinatario);

        } catch (Exception e) {
            log.error("Erro ao enviar email: {}", e.getMessage());
        }
    }
}
