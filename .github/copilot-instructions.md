# Instruções do projeto ConectaClinica

## Contexto da aplicação

O ConectaClinica é uma API RESTful para gestão de clínicas médicas. A aplicação deverá apoiar o cadastro e a gestão de pacientes, médicos, funcionários, consultas e prontuários, além de permitir integração com aplicações, portais de atendimento e outros sistemas de saúde.

O projeto atual é um backend desenvolvido com:

- Java 21;
- Spring Boot;
- Spring MVC;
- Spring Data JPA;
- PostgreSQL;
- Spring Security;
- autenticação baseada em JWT;
- Bean Validation;
- RabbitMQ;
- envio de e-mails via SMTP;
- Maven;
- Lombok;
- OpenAPI/Swagger.

## Estrutura atual

O código principal está em:

```text
src/main/java/com/felipefreitas/ConectaClinica
```

Os principais pacotes são:

- `config`: configurações do Spring, segurança e OpenAPI;
- `controller`: endpoints REST;
- `dto`: objetos de entrada e saída da API;
- `entity`: entidades persistidas no banco;
- `enums`: enumerações do domínio;
- `exceptions`: exceções e tratamento global de erros;
- `repository`: interfaces de acesso a dados;
- `security`: autenticação, autorização e JWT;
- `service`: regras de negócio;
- `util`: utilitários reutilizáveis.

Configurações de ambiente ficam em:

```text
src/main/resources/application.properties
```

Nunca inclua senhas, tokens, chaves JWT ou credenciais SMTP diretamente no código ou em arquivos versionados. Utilize variáveis de ambiente e preserve os valores sensíveis existentes.

## Regras de implementação

1. Antes de alterar o código, examine as classes e os padrões já existentes e reutilize soluções existentes.
2. Preserve a separação entre controller, DTO, service, repository e entity.
3. Controllers devem ser responsáveis por HTTP, validação de entrada e conversão de respostas; regras de negócio devem ficar nos services.
4. Não exponha entidades JPA diretamente nos endpoints. Use DTOs para requisições e respostas.
5. Use validações do Bean Validation nos DTOs e trate erros de forma consistente com o `GlobalExceptionHandler`.
6. Proteja endpoints conforme as regras de autenticação e autorização já definidas em `SecurityConfig`.
7. Use nomes em português apenas quando isso já estiver estabelecido pelo domínio; mantenha nomes técnicos e APIs consistentes com o código existente.
8. Não altere contratos existentes sem avaliar compatibilidade com os consumidores da API.
9. Não adicione dependências sem necessidade. Se uma dependência for indispensável, atualize o `pom.xml` de forma explícita.
10. Não introduza `catch` genérico nem silencie erros. Erros devem ser propagados ou tratados com uma resposta adequada à API.
11. Ao criar uma funcionalidade, implemente o fluxo completo necessário: entidade, repository, service, DTOs, controller, validações, segurança e testes, quando aplicável.
12. Use migrations ou uma estratégia de evolução de banco compatível com o projeto antes de introduzir mudanças persistentes relevantes.
13. Preserve o comportamento existente e faça alterações cirúrgicas, sem modificar arquivos não relacionados.
14. Não versione arquivos de build, credenciais, tokens, logs ou configurações locais.

## Padrão esperado para novas funcionalidades

Para cada funcionalidade:

1. Identifique o caso de uso e as regras de negócio.
2. Verifique entidades, enums, repositories, services e endpoints relacionados.
3. Defina DTOs de entrada e saída com validações apropriadas.
4. Implemente a regra de negócio no service.
5. Exponha somente os endpoints necessários no controller.
6. Adicione ou atualize o tratamento de erros.
7. Avalie autenticação, autorização e impactos no banco.
8. Crie ou atualize testes relevantes.
9. Execute os testes e corrija falhas introduzidas pela alteração.

## Comandos principais

No Windows:

```text
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:

```text
./mvnw test
./mvnw spring-boot:run
```

## Exemplo de solicitação para as próximas tarefas

Use este formato como referência:

```text
Siga as instruções de .github/copilot-instructions.md.

Implemente o cadastro de pacientes no ConectaClinica.

Requisitos:
- criar os DTOs de entrada e saída;
- criar a entidade e o repository;
- implementar as regras no service;
- criar os endpoints REST no controller;
- validar os campos obrigatórios;
- impedir duplicidade de CPF;
- proteger os endpoints conforme a configuração de segurança;
- tratar erros pelo padrão global da aplicação;
- criar ou atualizar os testes necessários.

Antes de editar, analise os padrões já existentes no projeto. Ao final, informe os arquivos alterados e os testes executados.
```

Quando os requisitos de negócio estiverem incompletos ou houver mais de uma decisão razoável de comportamento, solicite esclarecimento antes de escolher uma regra que altere o escopo da aplicação.
