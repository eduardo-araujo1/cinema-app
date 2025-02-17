package com.eduardo.cinema_app.config;

import com.eduardo.cinema_app.domain.Ticket;
import com.eduardo.cinema_app.enums.Status;
import com.eduardo.cinema_app.repositories.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class TicketScheduler {

    private final TicketRepository ticketRepository;

    public TicketScheduler(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void deleteExpiredTickets() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);

        List<Ticket> expiredTickets = ticketRepository
                .findByStatusAndCreatedAtLessThan(Status.AGUARDANDO_PAGAMENTO, fifteenMinutesAgo);

        if (!expiredTickets.isEmpty()) {
            log.info("Deletando {} tickets expirados", expiredTickets.size());
            ticketRepository.deleteAll(expiredTickets);
        }
    }
}
