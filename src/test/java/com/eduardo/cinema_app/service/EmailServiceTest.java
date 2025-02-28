package com.eduardo.cinema_app.service;

import com.eduardo.cinema_app.domain.Ticket;
import com.eduardo.cinema_app.services.EmailService;
import com.eduardo.cinema_app.util.TicketTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = TicketTestUtil.createTicket();
    }

    @Test
    void shouldSendPaymentConfirmationEmail() {
        emailService.sendPaymentConfirmationEmail(ticket);

        ArgumentCaptor<SimpleMailMessage> emailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(emailCaptor.capture());

        SimpleMailMessage sentMessage = emailCaptor.getValue();
        assertEquals(ticket.getCustomer().getEmail(), Objects.requireNonNull(sentMessage.getTo())[0]);
        assertEquals("Confirmação de pagamento - Ingresso#" + ticket.getId(), sentMessage.getSubject());
        assertNotNull(sentMessage.getText());
    }

    @Test
    void shouldHandleExceptionWhenEmailFails() {
        doThrow(new RuntimeException("Erro no envio")).when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendPaymentConfirmationEmail(ticket));

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
