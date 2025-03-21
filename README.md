# 🎬 Cinema App

Um sistema completo para gerenciamento de cinema, com funcionalidades para reserva de ingressos, gerenciamento de filmes, sessões e salas.

## 🚀 Funcionalidades

### 🔐 Usuários e Autenticação
- **Autenticação e autorização**: Registro, login e controle de acesso

### 🎥 Gerenciamento de Conteúdo
- Cadastro e gerenciamento de filmes
- Cadastro e gerenciamento de salas de cinema
- Agendamento de sessões
- Upload e armazenamento de imagens (pôsteres) no **AWS S3**

### 🎟️ Reserva de Ingressos
- Visualização de sessões disponíveis
- Seleção de assentos com **bloqueio concorrente**
- Emissão de tickets com identificador único

### 💳 Pagamento
- Integração com **Stripe** para pagamento com cartão
- Processamento seguro de transações
- Confirmação de pagamento

### 📩 Notificações
- Envio de **e-mails de confirmação**
- Detalhes do ingresso e informações da sessão

### Expiração de Reservas
- Scheduler que expira tickets não pagos em 15 minutos
- Liberação automática dos assentos

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java com Spring Boot**
- **Spring Security** para autenticação e autorização
- **Spring Data JPA** para persistência
- **Lombok** para redução de código boilerplate
- **Transações com bloqueio pessimista** para assentos

### Banco de Dados
- **Postgresql**

### Serviços Externos
- **AWS S3** para armazenamento de imagens
- **Stripe** para processamento de pagamentos
- **JavaMailSender** para envio de e-mails

## 🏛️ Arquitetura
O sistema segue uma arquitetura em camadas:

- **Entidades**: `Customer`, `Movie`, `Room`, `Seat`, `Session`, `Ticket`, `TicketSeat`
- **Repositórios**: Acesso a dados e operações CRUD
- **Serviços**: Lógica de negócio e orquestração de operações
- **Controladores**: APIs RESTful para interação com o frontend
- **DTOs**: Objetos de transferência de dados para comunicação entre camadas

## 🔄 Fluxo Principal do Sistema
1. Um **administrador** cadastra filmes, salas e programa sessões
2. **Usuários** podem visualizar os filmes e sessões disponíveis
3. Um usuário seleciona uma sessão e escolhe assentos disponíveis
4. O sistema **reserva temporariamente os assentos** (com bloqueio pessimista)
5. O usuário é redirecionado para o **Stripe** para pagamento
6. Após pagamento confirmado, o **ticket** é emitido
7. Um **e-mail de confirmação** é enviado ao usuário com os detalhes do ingresso
8. Caso o pagamento não seja realizado em 15 minutos, o ticket expira e os assentos são liberados


## ⚙️ Destaques Técnicos

### 🔄 Concorrência e Segurança
- **Bloqueio pessimista** para reserva de assentos, evitando conflitos
- **Transações atômicas** para garantir integridade dos dados
- **Autenticação com tokens JWT**

### 🔗 Integração com Serviços Externos
- **Armazenamento de imagens** no AWS S3
- **Processamento de pagamentos** via Stripe
- **Envio assíncrono de e-mails**


## 🛠️ Configuração e Instalação

### 📌 Pré-requisitos
- **Java 21**
- **Maven**
- **Banco de dados relacional** (PostgreSQL)
- **Conta AWS para o bucket S3**
- **Conta Stripe**
- **Configuração de servidor SMTP**

## Endpoints

### Autenticação
#### `POST /auth/login`
- **Descrição:** Realiza login do usuário.
- **Resposta:** Retorna o token de autenticação.

#### `POST /auth/register`
- **Descrição:** Cria um novo usuário.

### Filmes
#### `POST /movies`
- **Descrição:** Cadastra um novo filme (Requer ROLE_ADMIN).

#### `GET /movies`
- **Descrição:** Retorna uma lista paginada de filmes, podendo filtrar por título e gênero.

#### `GET /movies/{id}/sessions`
- **Descrição:** Retorna detalhes do filme e as sessões agendadas.

#### `PUT /movies/{id}`
- **Descrição:** Atualiza um filme existente (Requer ROLE_ADMIN).

#### `DELETE /movies/{id}`
- **Descrição:** Exclui um filme e suas sessões associadas (Requer ROLE_ADMIN).

### Salas
#### `POST /rooms`
- **Descrição:** Cria uma sala de cinema (Requer ROLE_ADMIN).

### Sessões
#### `POST /sessions`
- **Descrição:** Cria uma sessão para determinado filme (Requer ROLE_ADMIN).

#### `GET /sessions/{sessionId}/seats`
- **Descrição:** Retorna assentos disponíveis para uma sessão.

### Tickets
#### `POST /tickets`
- **Descrição:** Reserva um ticket e cria a URL para pagamento no Stripe.
- **Resposta:** Ticket reservado.

#### `GET /tickets/success/{ticketId}`
- **Descrição:** Redireciona para esta URL em caso de pagamento bem-sucedido via Stripe.
- **Resposta:** Pagamento realizado.

#### `GET /tickets/failure/{ticketId}`
- **Descrição:** Redireciona para esta URL em caso de falha no pagamento via Stripe.
- **Resposta:** Falha no pagamento.
