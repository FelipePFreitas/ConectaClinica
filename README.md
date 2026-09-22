# ConectaClínica API

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4.1.1">
  <img src="https://img.shields.io/badge/Spring_Security-JWT-6DB33F?logo=springsecurity&logoColor=white" alt="Spring Security e JWT">
  <img src="https://img.shields.io/badge/PostgreSQL-16+-4169E1?logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Hibernate/JPA-59666C?logo=hibernate&logoColor=white" alt="Hibernate e JPA">
  <img src="https://img.shields.io/badge/RabbitMQ-5672-FF6600?logo=rabbitmq&logoColor=white" alt="RabbitMQ">
  <img src="https://img.shields.io/badge/OpenAPI-3-6BA539?logo=openapiinitiative&logoColor=white" alt="OpenAPI 3">
  <img src="https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/Docker_Compose-2496ED?logo=docker&logoColor=white" alt="Docker Compose">
</p>

API REST para apoiar a gestão de clínicas médicas. O projeto centraliza autenticação, cadastro de funcionários, pacientes e profissionais, agendamento de consultas e bloqueios de agenda, com rastreabilidade das alterações e validações de conflito.

> **Status:** em desenvolvimento. Os recursos descritos como implementados abaixo refletem o código atual; prontuário eletrônico, permissões detalhadas por perfil e notificações completas ainda estão em evolução.

## Sumário

- [Visão geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Executando localmente](#executando-localmente)
- [Configuração](#configuração)
- [Autenticação](#autenticação)
- [API](#api)
- [Regras de agendamento](#regras-de-agendamento)
- [Documentação interativa](#documentação-interativa)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Roadmap](#roadmap)

## Visão geral

A ConectaClínica oferece uma API stateless para aplicações web, mobile e portais de atendimento. A API:

- gerencia usuários e funcionários da clínica;
- cadastra e consulta pacientes e profissionais;
- cria consultas com paciente, profissional, duração, modalidade e local/canal;
- impede sobreposição de consultas para o paciente e para o profissional;
- permite bloquear períodos da agenda;
- permite alterar status, cancelar e reagendar consultas;
- registra autor, data, hora e histórico das principais alterações;
- protege os endpoints privados com JWT e senhas com BCrypt;
- disponibiliza contrato OpenAPI/Swagger para integração.

## Tecnologias

| Categoria | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Framework web | Spring Boot 4.1.1, Spring MVC |
| Persistência | Spring Data JPA, Hibernate |
| Banco de dados | PostgreSQL |
| Segurança | Spring Security, JWT (`java-jwt`) e BCrypt |
| Validação | Jakarta Bean Validation |
| Mensageria | RabbitMQ via Spring AMQP |
| E-mail | Spring Boot Starter Mail, com SMTP/Mailtrap no ambiente local |
| Contrato da API | SpringDoc OpenAPI 3 e Swagger UI |
| Build | Maven |
| Ambiente local | Docker Compose |
| Testes | Spring Boot Test, JUnit e Mockito |

## Funcionalidades

### Implementadas

- Login com emissão de token Bearer JWT.
- Cadastro público de funcionário, com criação de usuário associado e envio de senha inicial por e-mail.
- Cadastro, listagem e consulta de pacientes.
- Cadastro, listagem e consulta de profissionais.
- Criação e consulta de consultas.
- Consulta por paciente ou profissional.
- Bloqueio e consulta de períodos indisponíveis da agenda.
- Transição controlada de status da consulta.
- Cancelamento com tipo e motivo obrigatórios.
- Reagendamento com preservação do histórico.
- Validação de datas futuras, duração positiva, profissional ativo e conflitos de agenda.
- Respostas REST para falhas de autenticação, autorização e erros de negócio.

### Em definição ou expansão

- Prontuário eletrônico e histórico clínico.
- Perfis de acesso detalhados por função.
- Recuperação e troca de senha.
- Políticas de confirmação, faltas, encaixes e recorrência.
- Notificações transacionais completas para pacientes e profissionais.
- Regras de LGPD, retenção e auditoria de acesso a dados clínicos.

## Arquitetura

O código está organizado por responsabilidades:

```text
src/main/java/com/felipefreitas/ConectaClinica/
├── config/          # Segurança e configuração do OpenAPI
├── controller/      # Endpoints REST
├── dto/             # Objetos de entrada e saída da API
├── entity/          # Entidades persistidas
├── enums/           # Status, modalidades e tipos de operação
├── exceptions/      # Exceções e tratamento global
├── repository/      # Repositórios Spring Data
├── security/        # Filtro JWT e componentes de autenticação
├── service/         # Regras de negócio e transações
└── util/            # Utilitários compartilhados
```

Os controllers recebem e validam os DTOs, enquanto as regras que dependem de persistência ficam nos services. As entidades não são expostas diretamente: as respostas são convertidas para DTOs.

## Executando localmente

### Pré-requisitos

- JDK 21;
- Docker e Docker Compose;
- Maven instalado ou uso do Maven Wrapper incluído (`mvnw`/`mvnw.cmd`).

### 1. Subir as dependências

```bash
docker compose up -d
```

Isso inicia:

| Serviço | Porta | Uso |
| --- | ---: | --- |
| PostgreSQL | `5432` | Banco `conectaclinica` |
| RabbitMQ | `5672` | Mensageria |

### 2. Iniciar a API

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

A aplicação ficará disponível em `http://localhost:8080`.

## Configuração

As propriedades possuem valores locais padrão, mas os ambientes devem fornecer os valores por variáveis de ambiente:

| Variável | Padrão local | Descrição |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/conectaclinica` | URL do PostgreSQL |
| `DB_USER` | `myuser` | Usuário do banco |
| `DB_PASSWORD` | definido no ambiente | Senha do banco |
| `JPA_DDL_AUTO` | `update` | Estratégia de schema do Hibernate |
| `RABBITMQ_HOST` | `localhost` | Host do RabbitMQ |
| `RABBITMQ_PORT` | `5672` | Porta do RabbitMQ |
| `RABBITMQ_USER` | `myuser` | Usuário do RabbitMQ |
| `RABBITMQ_PASSWORD` | definido no ambiente | Senha do RabbitMQ |
| `JWT_SECRET` | definido no ambiente | Chave de assinatura dos tokens |
| `JWT_EXPIRATION` | `86400000` | Expiração do token em milissegundos |

Não versionar senhas, chaves JWT ou credenciais SMTP. Para produção, substitua todos os valores de desenvolvimento e use um gerenciador de segredos.

## Autenticação

A API utiliza sessões stateless e o cabeçalho:

```http
Authorization: Bearer <accessToken>
```

O login é público:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"login\":\"CPF_OU_LOGIN\",\"senha\":\"SUA_SENHA\"}"
```

Resposta de sucesso:

```json
{
  "tokenType": "Bearer",
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresInMillis": 86400000
}
```

Também são públicos o cadastro de funcionários, o Swagger/OpenAPI e os endpoints de health/info configurados pelo Spring Boot. Os demais endpoints exigem autenticação.

## API

**URL base local:** `http://localhost:8080`

Todos os corpos abaixo usam `Content-Type: application/json`. Identificadores são UUID e datas usam ISO-8601 com offset, por exemplo `2026-09-23T10:00:00-03:00`.

### Autenticação e usuários

| Método | Endpoint | Auth | Descrição | Status |
| --- | --- | :---: | --- | ---: |
| `POST` | `/auth/login` | Não | Autentica por login e senha e retorna JWT Bearer. | `200` |
| `POST` | `/funcionarios` | Não* | Cria funcionário, usuário associado e envia a senha inicial por e-mail. | `200` |

Exemplo de cadastro de funcionário:

```json
{
  "nome": "João da Silva",
  "cpf": "00000000000",
  "email": "joao@clinica.com"
}
```

### Pacientes

| Método | Endpoint | Auth | Descrição | Status |
| --- | --- | :---: | --- | ---: |
| `POST` | `/pacientes` | Sim | Cadastra um paciente. | `201` |
| `GET` | `/pacientes` | Sim | Lista pacientes. | `200` |
| `GET` | `/pacientes/{id}` | Sim | Busca um paciente por UUID. | `200` |

Exemplo:

```json
{
  "nome": "Maria Souza"
}
```

### Profissionais

| Método | Endpoint | Auth | Descrição | Status |
| --- | --- | :---: | --- | ---: |
| `POST` | `/profissionais` | Sim | Cadastra um profissional. | `201` |
| `GET` | `/profissionais` | Sim | Lista profissionais. | `200` |
| `GET` | `/profissionais/{id}` | Sim | Busca um profissional por UUID. | `200` |

Exemplo:

```json
{
  "nome": "Dra. Ana Lima",
  "ativo": true
}
```

### Consultas

| Método | Endpoint | Auth | Descrição | Status |
| --- | --- | :---: | --- | ---: |
| `POST` | `/consultas` | Sim | Agenda uma consulta com validação de conflitos. | `201` |
| `GET` | `/consultas/{id}` | Sim | Busca uma consulta por UUID. | `200` |
| `GET` | `/consultas?pacienteId={uuid}` | Sim | Lista consultas de um paciente. | `200` |
| `GET` | `/consultas?profissionalId={uuid}` | Sim | Lista consultas de um profissional. | `200` |
| `PATCH` | `/consultas/{id}/status` | Sim | Altera o status respeitando as transições permitidas. | `200` |
| `PATCH` | `/consultas/{id}/cancelamento` | Sim | Cancela com tipo e motivo. | `200` |
| `PATCH` | `/consultas/{id}/reagendamento` | Sim | Reagenda para um novo início. | `200` |

Exemplo de agendamento:

```json
{
  "pacienteId": "00000000-0000-0000-0000-000000000001",
  "profissionalId": "00000000-0000-0000-0000-000000000002",
  "inicio": "2026-09-23T10:00:00-03:00",
  "duracaoMinutos": 30,
  "modalidade": "PRESENCIAL",
  "localOuCanal": "Sala 2"
}
```

Modalidades aceitas: `PRESENCIAL` e `TELEATENDIMENTO`.

Status de consulta: `SOLICITADA`, `AGENDADA`, `CONFIRMADA`, `EM_ATENDIMENTO`, `CONCLUIDA`, `CANCELADA_PELO_PACIENTE`, `CANCELADA_PELA_CLINICA`, `REAGENDAMENTO_SOLICITADO` e `NAO_COMPARECEU`.

Exemplo de alteração de status:

```json
{
  "status": "CONFIRMADA",
  "motivo": "Paciente confirmou pelo telefone"
}
```

Exemplo de cancelamento:

```json
{
  "tipo": "PACIENTE",
  "motivo": "Solicitado pelo paciente"
}
```

Exemplo de reagendamento:

```json
{
  "inicio": "2026-09-24T11:00:00-03:00",
  "duracaoMinutos": 30,
  "motivo": "Indisponibilidade no horário anterior"
}
```

### Bloqueios de agenda

| Método | Endpoint | Auth | Descrição | Status |
| --- | --- | :---: | --- | ---: |
| `POST` | `/bloqueios-agenda` | Sim | Bloqueia um período futuro do profissional. | `201` |
| `GET` | `/bloqueios-agenda?profissionalId={uuid}` | Sim | Lista bloqueios de um profissional. | `200` |

Exemplo:

```json
{
  "profissionalId": "00000000-0000-0000-0000-000000000002",
  "inicio": "2026-09-25T12:00:00-03:00",
  "fim": "2026-09-25T14:00:00-03:00",
  "motivo": "Reunião interna"
}
```

### Validações e erros

As entradas são validadas com Jakarta Bean Validation. Entre as validações aplicadas estão campos obrigatórios, e-mail válido, datas futuras, duração positiva, profissional ativo, conflitos de agenda e transições de status. A API possui tratamento REST global para erros de autenticação, autorização, validação e regras de negócio.

## Regras de agendamento

- Consultas só podem começar no futuro.
- O profissional precisa estar ativo.
- O paciente não pode ter consultas ativas sobrepostas.
- O profissional não pode ter consultas ativas sobrepostas.
- Um bloqueio não pode conflitar com consultas ou outro bloqueio do profissional.
- Consultas encerradas não podem ser alteradas.
- Cancelamentos exigem tipo e motivo.
- Reagendamentos preservam a consulta e registram a alteração no histórico.
- O histórico registra criação, cancelamento, reagendamento e alteração de status com autor e timestamps.

As regras em construção e decisões de negócio pendentes estão detalhadas em [`docs/business-rules.md`](docs/business-rules.md).

## Documentação interativa

Com a aplicação em execução:

- Swagger UI: [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
- OpenAPI JSON: [`http://localhost:8080/v3/api-docs`](http://localhost:8080/v3/api-docs)
- OpenAPI YAML: [`http://localhost:8080/v3/api-docs.yaml`](http://localhost:8080/v3/api-docs.yaml)

No Swagger UI, use **Authorize** e informe `Bearer <token>` para testar endpoints privados.

## Testes

Executar toda a suíte:

```powershell
.\mvnw.cmd test
```

Os testes cobrem o carregamento da aplicação e cenários do serviço de consultas, incluindo sucesso, validações, conflitos e transições inválidas.

## Estrutura do projeto

```text
ConectaClinica/
├── src/main/java/       # Código da API
├── src/main/resources/  # Configurações da aplicação
├── src/test/java/       # Testes automatizados
├── docs/                # Regras de negócio
├── compose.yaml         # PostgreSQL e RabbitMQ
├── pom.xml              # Dependências e build Maven
└── README.md
```

## Roadmap

- [ ] Consolidar autorização por perfil e clínica.
- [ ] Implementar prontuário eletrônico com auditoria.
- [ ] Definir recuperação e troca de senha.
- [ ] Finalizar notificações por e-mail e mensageria.
- [ ] Formalizar regras de LGPD e retenção.
- [ ] Expandir cobertura de testes de controllers e segurança.

## Licença e contato

O projeto utiliza a licença Apache 2.0. Para dúvidas ou contribuições, acesse o [repositório no GitHub](https://github.com/FelipePFreitas/ConectaClinica) ou entre em contato com [Felipe Freitas](https://github.com/FelipePFreitas).
