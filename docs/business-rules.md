# Regras de negócio

Este documento registra as regras funcionais do ConectaClinica. Ele deve ser atualizado sempre que uma regra for definida ou alterada.

As regras descritas como **implementadas** refletem o comportamento atual do código. As regras descritas como **a definir** são pontos que precisam de decisão de negócio antes da implementação.

## 1. Cadastro de funcionários

### 1.1 Regras implementadas

- O nome do funcionário é obrigatório.
- O CPF do funcionário é obrigatório.
- O e-mail do funcionário é obrigatório.
- Não é permitido cadastrar dois funcionários com o mesmo CPF.
- Não é permitido cadastrar dois funcionários com o mesmo e-mail.
- O cadastro de funcionário cria também um usuário de acesso associado ao funcionário.
- O login inicial do usuário é o CPF do funcionário.
- A senha inicial é gerada automaticamente pelo sistema.
- A senha inicial é armazenada usando hash, nunca em texto puro.
- A senha inicial é enviada para o e-mail informado no cadastro.
- O usuário recebe a role `FUNCIONARIO` quando é criado.
- O cadastro do funcionário e a criação do usuário fazem parte de uma operação transacional.

### 1.2 Regras a definir

- Formato permitido para CPF e e-mail.
- Necessidade de validar se o CPF é válido, além de verificar duplicidade.
- Possibilidade de alterar o CPF após o cadastro.
- Fluxo de troca da senha inicial.
- Comportamento quando o envio do e-mail falhar.
- Possibilidade de cadastrar outros perfis de funcionário.
- Regras de ativação, inativação e exclusão de funcionários.

## 2. Autenticação e autorização

### 2.1 Regras implementadas

- A autenticação é feita por login e senha.
- O login retorna um token no formato Bearer.
- A API utiliza JWT para autenticação.
- As sessões do servidor são stateless.
- Senhas são verificadas usando BCrypt.
- Endpoints não públicos exigem autenticação.
- A documentação OpenAPI/Swagger pode ser acessada sem autenticação.
- O endpoint de login pode ser acessado sem autenticação.
- O cadastro de funcionários pode ser acessado sem autenticação enquanto estiver configurado como endpoint público.
- Falhas de autenticação e autorização devem retornar respostas tratadas pelos handlers REST globais.

### 2.2 Regras a definir

- Quais perfis podem cadastrar funcionários.
- Quais perfis podem acessar cada recurso da clínica.
- Tempo de expiração e política de renovação dos tokens.
- Fluxo de recuperação e redefinição de senha.
- Política de bloqueio após tentativas de login inválidas.
- Necessidade de auditoria de logins e alterações sensíveis.

## 3. Pacientes

Este domínio está previsto no objetivo da aplicação, mas ainda não possui implementação no código atual.

### 3.1 Regras a definir

- Dados obrigatórios para o cadastro.
- Identificador único do paciente.
- Validação e unicidade do CPF.
- Consentimento e tratamento de dados pessoais conforme a LGPD.
- Situações de ativo, inativo e bloqueado.
- Permissões de acesso aos dados clínicos.
- Regras de alteração e exclusão de dados.

## 4. Médicos e profissionais de saúde

Este domínio está previsto no objetivo da aplicação, mas ainda não possui implementação no código atual.

### 4.1 Regras a definir

- Dados profissionais obrigatórios.
- Registro profissional e sua validação.
- Especialidades e vínculos com clínicas.
- Situações de ativo e inativo.
- Permissões para acessar prontuários e consultas.

## 5. Agendamento de consultas

Este domínio está previsto no objetivo da aplicação, mas ainda não possui implementação no código atual.

### 5.1 Regras a definir

- Duração padrão de uma consulta.
- Impedimento de dois agendamentos para o mesmo médico no mesmo horário.
- Impedimento de conflitos de horário para o paciente.
- Antecedência mínima para agendamento e cancelamento.
- Status possíveis para uma consulta.
- Regras para faltas, reagendamentos e encaixes.
- Notificações para pacientes e profissionais.

## 6. Prontuários eletrônicos

Este domínio está previsto no objetivo da aplicação, mas ainda não possui implementação no código atual.

### 6.1 Regras a definir

- Quem pode criar, consultar e alterar um prontuário.
- Necessidade de manter histórico e versionamento.
- Imutabilidade de registros após o encerramento do atendimento.
- Auditoria de acesso e alterações.
- Prazo de retenção das informações.

## 7. Diretrizes para implementação

- Toda regra de negócio deve ser implementada na camada de serviço ou em um componente de domínio apropriado, e não diretamente no controller.
- Toda regra relevante deve possuir testes que cubram o comportamento esperado e os casos de erro.
- Validações de formato e obrigatoriedade devem ser feitas nos DTOs quando forem validações de entrada.
- Regras que dependem de dados persistidos devem ser verificadas também no service e protegidas por restrições adequadas no banco quando aplicável.
- Alterações em regras existentes devem atualizar este documento, a implementação e os testes relacionados.
- Quando uma regra estiver marcada como **a definir**, não deve ser inventada durante a implementação. Solicite uma decisão de negócio antes de escolher o comportamento.
