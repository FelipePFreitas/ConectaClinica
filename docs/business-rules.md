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

As regras abaixo são uma proposta funcional recomendada para validação do negócio. Elas não representam comportamento já implementado. Enquanto não forem aprovadas, devem ser tratadas como pendentes de decisão e não devem ser assumidas pelo desenvolvimento.

### 5.1 Regras recomendadas para definição

#### 5.1.1 Participantes e responsabilidade

- O paciente é a pessoa para quem a consulta será realizada e deve estar identificado antes da confirmação do agendamento.
- O profissional é a pessoa habilitada para realizar o atendimento e deve estar ativo e vinculado à especialidade compatível com o tipo de consulta.
- Apenas usuários autorizados podem agendar, alterar, cancelar ou confirmar consultas em nome de um paciente.
- O profissional pode consultar e administrar somente os horários e consultas sob sua responsabilidade, conforme seu perfil de acesso.
- O paciente pode consultar suas próprias consultas e solicitar alterações dentro dos prazos definidos.

#### 5.1.2 Disponibilidade

- Um horário só pode ser oferecido para agendamento quando estiver dentro da agenda de atendimento do profissional, da unidade e da modalidade da consulta.
- A disponibilidade deve considerar o dia, horário, duração da consulta, intervalos e bloqueios previamente registrados.
- Horários fora da disponibilidade do profissional não podem ser confirmados por agendamento regular.
- Um bloqueio de agenda deve impedir novos agendamentos no período bloqueado e não deve apagar o histórico de consultas já realizadas.
- Quando uma consulta for cancelada, o horário poderá voltar a ser oferecido, desde que não esteja bloqueado por outra regra ou reserva.

#### 5.1.3 Criação do agendamento

- Um agendamento só pode ser confirmado quando houver paciente, profissional, data, horário, duração, modalidade e local ou canal do atendimento.
- A data e o horário devem ser registrados com o fuso horário oficial da clínica.
- O sistema deve informar ao solicitante o resumo do agendamento e o status resultante antes de concluir a operação.
- O mesmo paciente não deve possuir duas consultas sobrepostas, ainda que sejam com profissionais diferentes.
- O mesmo profissional não deve possuir duas consultas sobrepostas, ainda que sejam para pacientes diferentes.
- Uma consulta não pode ser agendada em horário já ocupado por outra consulta ativa ou por bloqueio de agenda.
- Se dois pedidos tentarem reservar o mesmo horário, somente o primeiro confirmado deve ocupar a disponibilidade; o outro deve ser recusado com motivo claro.
- O agendamento deve registrar quem realizou a ação e em qual data e horário ela ocorreu.

#### 5.1.4 Status da consulta

Os status recomendados são:

- **Solicitada**: pedido recebido, mas ainda aguardando confirmação de um usuário autorizado ou de uma regra de negócio.
- **Agendada**: horário reservado e consulta confirmada para paciente e profissional.
- **Confirmada**: paciente ou responsável confirmou que comparecerá, dentro do prazo definido.
- **Em atendimento**: consulta iniciada pelo profissional.
- **Concluída**: atendimento encerrado.
- **Cancelada pelo paciente**: cancelamento solicitado pelo paciente ou responsável.
- **Cancelada pela clínica**: cancelamento realizado pela clínica ou pelo profissional.
- **Reagendamento solicitado**: pedido de mudança ainda não concluído.
- **Não compareceu**: consulta encerrada sem presença do paciente, após o prazo de tolerância definido.

Regras de transição:

- Uma consulta **Solicitada** pode tornar-se **Agendada** ou ser recusada.
- Uma consulta **Agendada** pode tornar-se **Confirmada**, **Cancelada** ou **Reagendamento solicitado**.
- Uma consulta **Confirmada** pode tornar-se **Em atendimento**, **Cancelada** ou **Não compareceu**, conforme as regras de prazo e presença.
- Uma consulta **Em atendimento** só pode tornar-se **Concluída** ou ser encerrada por uma ocorrência registrada.
- Uma consulta **Concluída**, **Cancelada** ou marcada como **Não compareceu** não pode voltar a ocupar o mesmo horário nem ser editada como se estivesse ativa.

#### 5.1.5 Cancelamento

- O paciente ou responsável pode solicitar cancelamento até a antecedência mínima definida pela clínica.
- A clínica ou o profissional pode cancelar uma consulta a qualquer momento quando houver indisponibilidade, motivo operacional ou necessidade assistencial.
- Todo cancelamento deve registrar autor, data, horário e motivo.
- Ao cancelar uma consulta futura, o horário deve ser liberado somente depois que o cancelamento estiver confirmado.
- O cancelamento fora do prazo deve ser permitido apenas para perfis autorizados ou situações excepcionais registradas.
- O cancelamento não deve apagar o registro da consulta nem seu histórico de alterações.

#### 5.1.6 Reagendamento

- O reagendamento deve preservar a consulta original, seu motivo e o usuário que solicitou a alteração.
- Um novo horário só pode ser confirmado se atender às mesmas regras de disponibilidade e conflito de horário de um novo agendamento.
- Até a confirmação do novo horário, a consulta original deve permanecer ativa ou ser marcada como **Reagendamento solicitado**, sem liberar o horário indevidamente.
- Quando o novo horário for confirmado, o horário anterior deve ser liberado e o paciente e o profissional devem receber a atualização.
- Reagendamentos sucessivos devem permanecer no histórico para auditoria.

#### 5.1.7 Confirmação, faltas e tolerância

- A confirmação deve ser solicitada para consultas futuras dentro da janela de antecedência definida pela clínica.
- A ausência de resposta à solicitação de confirmação não deve, por si só, cancelar a consulta, salvo decisão expressa da clínica.
- O paciente deve poder informar que não comparecerá, seguindo as regras de cancelamento ou reagendamento aplicáveis.
- O profissional ou usuário autorizado deve registrar a falta somente após o início previsto da consulta e após o prazo de tolerância definido.
- Não se deve marcar falta quando houver registro de atendimento iniciado ou cancelamento confirmado antes do horário.
- Regras de reincidência de faltas não devem bloquear novos agendamentos sem decisão específica e comunicação ao paciente.

#### 5.1.8 Antecedência e encaixes

- O agendamento deve respeitar uma antecedência mínima em relação ao início da consulta.
- O cancelamento e o reagendamento devem respeitar prazos próprios, que podem ser diferentes do prazo de agendamento.
- O sistema deve recusar datas passadas e horários cujo início já tenha ocorrido.
- Encaixes só podem ser realizados por usuário autorizado, em intervalo declarado como disponível ou com justificativa registrada.
- Um encaixe não pode gerar sobreposição de consultas nem reduzir o intervalo mínimo definido para o profissional.

#### 5.1.9 Notificações

- O paciente deve receber confirmação ou recusa do agendamento, cancelamento, reagendamento e alteração de status relevante.
- O profissional deve receber aviso sobre novos agendamentos, cancelamentos, reagendamentos e consultas próximas sob sua responsabilidade.
- A notificação deve informar paciente, profissional, data, horário, modalidade, local ou canal e status da consulta.
- O envio de uma notificação não substitui o registro da consulta nem altera seu status por si só.
- Falha no envio deve ser registrada para acompanhamento, sem desfazer automaticamente um agendamento já confirmado.
- O canal e o prazo das notificações devem respeitar os contatos autorizados pelo paciente e as regras de proteção de dados.

#### 5.1.10 Autorização e privacidade

- O acesso às consultas deve respeitar o papel do usuário, o vínculo com a clínica e a necessidade de atendimento.
- O paciente não pode consultar ou alterar dados de outro paciente sem autorização registrada.
- O profissional deve acessar somente as consultas necessárias para sua atuação e os dados compatíveis com suas permissões.
- Alterações realizadas por usuários administrativos devem identificar o responsável e o motivo quando a ação impactar uma consulta futura.
- Dados usados em notificações devem ser limitados ao necessário para identificar e realizar o atendimento, observando a LGPD.

#### 5.1.11 Auditoria e histórico

- Criação, confirmação, cancelamento, reagendamento, alteração de status, registro de falta e encaixe devem gerar histórico.
- O histórico deve informar a ação, o status anterior, o status posterior, o autor, a data e o horário.
- Registros de auditoria não devem ser apagados ou alterados como se a ação original não tivesse ocorrido.
- Consultas canceladas ou concluídas devem permanecer consultáveis para fins de atendimento, gestão e auditoria, conforme a política de retenção aprovada.

### 5.2 Pontos que ainda dependem de decisão de negócio

- Duração padrão por especialidade, tipo e modalidade de consulta.
- Fuso horário oficial e unidades que poderão utilizar a agenda.
- Identificação mínima do paciente e necessidade de responsável para menores ou pessoas dependentes.
- Perfis autorizados a agendar, confirmar, cancelar, reagendar, registrar faltas e realizar encaixes.
- Necessidade de aprovação manual para consultas solicitadas e prazo para essa aprovação.
- Antecedência mínima para agendamento, cancelamento e reagendamento.
- Janela e canal das confirmações e notificações (por exemplo, e-mail, mensagem ou outro canal autorizado).
- Prazo de tolerância para caracterizar não comparecimento.
- Regras para cobrança, multa, bloqueio ou restrição após cancelamentos tardios e faltas reincidentes.
- Possibilidade de encaixe, quantidade máxima de encaixes por período e necessidade de justificativa.
- Política para consultas recorrentes, retornos, teleatendimento e atendimentos em grupo.
- Tratamento de feriados, recessos, indisponibilidade emergencial e alterações de horário de verão.
- Prazo de retenção do histórico de consultas e dos registros de auditoria.
- Comportamento quando a notificação falhar ou quando o paciente não possuir canal de contato autorizado.

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
