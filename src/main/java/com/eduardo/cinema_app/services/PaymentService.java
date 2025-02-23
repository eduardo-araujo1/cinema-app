package com.eduardo.cinema_app.services;

import com.eduardo.cinema_app.dtos.response.PaymentResponseDTO;
import com.eduardo.cinema_app.dtos.response.TicketResponseDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public PaymentResponseDTO createPaymentLink(TicketResponseDTO ticket) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/tickets/success/" + ticket.id())
                .setCancelUrl("http://localhost:8080/tickets/fail")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("BRL")
                                .setUnitAmount(ticket.price().multiply(BigDecimal.valueOf(100)).longValueExact())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("cinema-app")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);

        return new PaymentResponseDTO(session.getUrl(), ticket);
    }
}
