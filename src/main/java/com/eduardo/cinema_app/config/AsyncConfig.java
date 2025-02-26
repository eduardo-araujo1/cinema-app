package com.eduardo.cinema_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor(); // Implementação do Spring baseada em um pool de threads.
        executor.setCorePoolSize(2); // Define o número mínimo de threads que ficarão sempre ativas
        executor.setMaxPoolSize(10); // Define o número máximo de threads que podem ser criadas
        executor.setQueueCapacity(500); // Define o tamanho da fila para tarefas pendentes
        executor.setThreadNamePrefix("EmailAsync-"); // Define um nome pras threads, facilitando a identificação em logs e debugging.
        executor.initialize();
        return executor;
    }
}
