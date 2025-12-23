package com.clinica_service.clinica_service.service;

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
    public void enviarEmailAtendimentoRealizado(String destinatario, String nomePaciente,
                                            String especialidade, String horario, String codigo) {
        if (!enabled) {
            log.info("Envio de email desabilitado");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(destinatario);
            message.setSubject("Atendimento Realizado - Hospital Management");
            message.setText(String.format(
                    "Olá %s,\n\n" +
                            "Seu atendimento foi realizado na data de: %s\n\n" +
                            "Detalhes:\n" +
                            "- Especialidade: %s\n" +
                            "- Data/Hora: %s\n" +
                            "- Código: %s\n\n" +
                            "Atenciosamente,\n" +
                            "Hospital Management",
                    nomePaciente, horario, especialidade, horario, codigo
            ));

            mailSender.send(message);
            log.info("Email enviado com sucesso para: {}", destinatario);

        } catch (Exception e) {
            log.error("Erro ao enviar email: {}", e.getMessage());
        }
    }

}