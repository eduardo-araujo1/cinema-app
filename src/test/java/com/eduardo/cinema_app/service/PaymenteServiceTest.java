package com.eduardo.cinema_app.service;

import com.eduardo.cinema_app.dtos.response.PaymentResponseDTO;
import com.eduardo.cinema_app.dtos.response.TicketResponseDTO;
import com.eduardo.cinema_app.services.PaymentService;
import com.eduardo.cinema_app.util.TicketTestUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymenteServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private Session sessionMock;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(paymentService, "stripeSecretKey", "sk_test_123");
    }

    @Test
    void createPaymentLink_ShouldReturnPaymentResponseDTO() throws StripeException {
        TicketResponseDTO ticket = TicketTestUtil.createTicketResponseDTO();
        Session sessionMock = mock(Session.class);
        when(sessionMock.getUrl()).thenReturn("https://mocked-stripe-url.com");

        try (MockedStatic<Session> mockedStatic = mockStatic(Session.class)) {
            mockedStatic.when(() -> Session.create(any(SessionCreateParams.class))).thenReturn(sessionMock);

            PaymentResponseDTO response = paymentService.createPaymentLink(ticket);


            assertEquals("https://mocked-stripe-url.com", response.payment_url());
            assertEquals(ticket, response.ticket());
            mockedStatic.verify(() -> Session.create(any(SessionCreateParams.class)), times(1));
        }

    }

}
