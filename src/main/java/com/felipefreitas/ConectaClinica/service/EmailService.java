package com.felipefreitas.ConectaClinica.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Envia o e-mail de confirmação de agendamento de forma assíncrona.
     */
    @Async
    public void enviarEmailConfirmacaoAgendamento(
            String destinatario,
            String nomePaciente,
            String nomeMedicoOuExame,
            LocalDateTime dataHora
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String dataFormatada = dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaFormatada = dataHora.format(DateTimeFormatter.ofPattern("HH:mm"));

            helper.setTo(destinatario);
            helper.setSubject("ConectaClínica 🩺 - Confirmação de Agendamento");

            String corpoHtml = String.format("""
                <div style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
                    <h2>Olá, %s!</h2>
                    <p>Seu agendamento na <strong>ConectaClínica</strong> foi realizado com sucesso.</p>
                    <hr style="border: 0; border-top: 1px solid #eee;"/>
                    <p><strong>Detalhes do Agendamento:</strong></p>
                    <ul>
                        <li><strong>Profissional/Procedimento:</strong> %s</li>
                        <li><strong>Data:</strong> %s</li>
                        <li><strong>Horário:</strong> %s hs</li>
                    </ul>
                    <p>Caso precise remarcar ou cancelar, entre em contato conosco com antecedência.</p>
                    <br/>
                    <p>Atenciosamente,<br/><strong>Equipe ConectaClínica</strong></p>
                </div>
                """, nomePaciente, nomeMedicoOuExame, dataFormatada, horaFormatada);

            helper.setText(corpoHtml, true); // O 'true' ativa a renderização HTML

            mailSender.send(message);
            log.info("E-mail de confirmação enviado com sucesso para: {}", destinatario);

        } catch (MessagingException e) {
            log.error("Erro ao montar/enviar e-mail de confirmação para {}: {}", destinatario, e.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao enviar e-mail para {}: {}", destinatario, e.getMessage());
        }
    }
}