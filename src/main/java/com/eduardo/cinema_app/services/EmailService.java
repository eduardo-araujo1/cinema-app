package com.eduardo.cinema_app.services;

import com.eduardo.cinema_app.domain.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("virtualThreadExecutor")
    public void sendPaymentConfirmationEmail(Ticket ticket) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(ticket.getCustomer().getEmail());
            message.setSubject("Confirmação de pagamento - Ingresso#" + ticket.getId());

            String emailBody = createEmailBody(ticket);
            message.setText(emailBody);

            mailSender.send(message);
            log.info("E-mail enviado com sucesso para {}", ticket.getCustomer().getEmail());
        } catch (Exception e) {
            log.error("Erro ao enviar email de confirmação para {}: {}", ticket.getCustomer().getEmail(), e.getMessage());
        }
    }

    private String createEmailBody(Ticket ticket) {
        StringBuilder body = new StringBuilder();
        body.append("Olá, ").append(ticket.getCustomer().getName()).append("!\n\n");
        body.append("Seu pagamento para o ingresso #").append(ticket.getId()).append(" foi confirmado com sucesso.\n\n");

        body.append("Detalhes do Ingresso:\n");
        body.append("- Filme: ").append(ticket.getSession().getMovie().getTitle()).append("\n");
        body.append("- Data/Hora: ").append(ticket.getSession().getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        body.append("- Sala: ").append(ticket.getSession().getRoom().getRoomNumber()).append("\n");

        body.append("\n Número do assento(s): ");
        List<String> seatNumbers = ticket.getTicketSeats().stream()
                .map(ts -> ts.getSeat().getSeatNumber())
                .collect(Collectors.toList());
        body.append(String.join(", ", seatNumbers)).append("\n");

        body.append("\nValor total: R$ ").append(ticket.getPrice()).append("\n\n");
        body.append("Apresente este e-mail ou o número do ingresso na entrada do cinema.\n\n");
        body.append("Agradecemos pela preferência!\n");
        body.append("Equipe do Cinema");

        return body.toString();
    }
}
