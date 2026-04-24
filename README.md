# Sistema de Gerenciamento Hospitalar

Sistema de gerenciamento hospitalar desenvolvido com arquitetura de microsserviços, permitindo agendamento de consultas e exames, atendimento clínico com sugestão de diagnósticos e gerenciamento de procedimentos cirúrgicos.

---

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Como Executar](#como-executar)
- [Endpoints Principais](#endpoints-principais)
- [Autenticação](#autenticação)
- [Testando a Aplicação](#testando-a-aplicação)

---

## Sobre o Projeto

Sistema distribuído para gerenciamento hospitalar com três microsserviços principais:

- **Agendamento Service**: Gerencia agendamentos de consultas e exames
- **Clínica Service**: Realiza atendimentos e sugere diagnósticos baseados em sintomas
- **Medicina Service**: Gerencia procedimentos cirúrgicos e exames de alta complexidade

---

README.md - Documentação Completa

Cria na raiz: Gerenciamento-Hospitalar/README.md
markdown# Sistema de Gerenciamento Hospitalar

Sistema de gerenciamento hospitalar desenvolvido com arquitetura de microsserviços, permitindo agendamento de consultas e exames, atendimento clínico com sugestão de diagnósticos e gerenciamento de procedimentos cirúrgicos.

---

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Como Executar](#como-executar)
- [Endpoints Principais](#endpoints-principais)
- [Autenticação](#autenticação)
- [Testando a Aplicação](#testando-a-aplicação)

---

## Sobre o Projeto

Sistema distribuído para gerenciamento hospitalar com três microsserviços principais:

- **Agendamento Service**: Gerencia agendamentos de consultas e exames
- **Clínica Service**: Realiza atendimentos e sugere diagnósticos baseados em sintomas
- **Medicina Service**: Gerencia procedimentos cirúrgicos e exames de alta complexidade

---

### **Comunicação:**
- **Síncrona (REST)**: Validações entre serviços
- **Assíncrona (RabbitMQ)**: Criação de consultas, exames e procedimentos

---

## Tecnologias

### **Backend:**
- Java 21
- Spring Boot 3.x
- Spring Cloud Gateway
- Spring Security + OAuth2
- Spring Data JPA
- Spring AMQP (RabbitMQ)
- Spring Mail
- Spring Cache (Redis)

### **Banco de Dados:**
- MySQL 8.0 (3 instâncias)
- Redis 7 (Cache)

### **Mensageria:**
- RabbitMQ 3

### **Autenticação:**
- Keycloak 26

### **Documentação:**
- Swagger/OpenAPI 3

### **Observabilidade:**
- Spring Actuator
- Métricas Prometheus

### **Containerização:**
- Docker
- Docker Compose

---

## Funcionalidades

### **Agendamento Service:**
-  Agendar consultas e exames
-  Validação de conflitos de horário
-  Validação de disponibilidade do médico
-  Validação de disponibilidade do paciente
-  Envio de email de confirmação
-  Consideração de duração dos procedimentos

### **Clínica Service:**
-  Receber consultas via RabbitMQ
-  Atender consultas
-  Sugerir diagnósticos baseados em sintomas
-  Sugerir tratamentos
-  Solicitar procedimentos de alta complexidade
-  Cache de consultas (Redis)
-  CRUD de sintomas, doenças, tratamentos e médicos

### **Medicina Service:**
- Receber exames via RabbitMQ
- Receber procedimentos da Clínica via RabbitMQ
- Gerenciar procedimentos de alta complexidade
- Validação de horários (emergenciais podem sobrepor)
- Controle de prioridades (baixa, padrão, alta, emergencial)

### **Recursos Transversais:**
- Autenticação JWT via Keycloak
- Autorização por roles (USUARIO, MEDICO, ADMIN)
- Global Exception Handler
- Documentação Swagger
- Health checks e métricas
- Logs estruturados

---

## Pré-requisitos

- Docker
- Docker Compose
- (Opcional) Java 21 + Maven (para desenvolvimento local)

---

## Como Executar

### **1. Clone o repositório:**
```bash
git clone https://github.com/seu-usuario/gerenciamento-hospitalar.git
cd gerenciamento-hospitalar
```

### **2. Suba todos os serviços:**
```bash
docker-compose up -d --build
```

**Aguarde ~5 minutos** para compilar e subir todos os containers.

### **3. Verifique o status:**
```bash
docker-compose ps
```

Todos os serviços devem estar **Up** ou **Up (healthy)**.

### **4. Acesse as aplicações:**

| Serviço | URL | Credenciais |
|---------|-----|-------------|
| **API Gateway** | http://localhost:8080 | - |
| **Keycloak** | http://localhost:8180 | admin / admin |
| **RabbitMQ** | http://localhost:15673 | guest / guest |
| **Swagger Agendamento** | http://localhost:8081/swagger-ui.html | - |
| **Swagger Clínica** | http://localhost:8082/swagger-ui.html | - |
| **Swagger Medicina** | http://localhost:8083/swagger-ui.html | - |

---

## Autenticação

### **Configurar Keycloak (Primeira Execução):**

1. Acesse: http://localhost:8180
2. Login: `admin` / `admin`
3. Crie o Realm `hospital`
4. Crie o Client `hospital-gateway`
5. Crie as Roles: `USUARIO`, `MEDICO`, `ADMIN`
6. Crie os usuários de teste:

### **Obter Token (Postman/cURL):**
```bash
POST http://localhost:8180/realms/hospital/protocol/openid-connect/token

Body (x-www-form-urlencoded):
grant_type: password
client_id: hospital-gateway
client_secret: [SEU_CLIENT_SECRET]
username: paciente
password: 123456
```

**Response:**
```json
{
  "access_token": "eyJhbGc...",
  "token_type": "Bearer"
}
```

---

## Endpoints Principais

### **Via API Gateway (Port 8080):**

**Todas as requisições precisam do header:**
```
Authorization: Bearer [ACCESS_TOKEN]
```

### **Agendamento:**
```http
POST /agendamento/api/cadastro/consulta
POST /agendamento/api/cadastro/exame
GET  /agendamento/api/agendamentos/{cpf}
PUT  /agendamento/api/consulta/atualizar/{cpf}/{id}
```

### **Clínica:**
```http
POST /clinica/api/clinica/atender-consulta
GET  /clinica/api/clinica/consultas/{cpf}
GET  /clinica/api/clinica/consultas/agendadas
POST /clinica/api/clinica/sintomas
POST /clinica/api/clinica/doencas
```

### **Medicina:**
```http
POST /medicina/api/procedimentos
POST /medicina/api/procedimentos/agendar-horario
POST /medicina/api/exames
GET  /medicina/api/procedimentos/{cpf}
```

### **Observabilidade:**
```http
GET /agendamento/actuator/health
GET /clinica/actuator/health
GET /medicina/actuator/health
GET /agendamento/actuator/metrics
```

---

## Testando a Aplicação

### **1. Obter Token:**

Use Postman ou cURL para obter token do Keycloak (usuário: `paciente`).

### **2. Agendar Consulta:**
```http
POST http://localhost:8080/agendamento/api/cadastro/consulta

Headers:
Authorization: Bearer [TOKEN]
Content-Type: application/json

Body:
{
  "paciente": {
    "nome": "Maria Silva",
    "cpf": "111.222.333-44",
    "idade": 35,
    "sexo": "Feminino",
    "email": "maria@example.com"
  },
  "horario": "2025-01-20T10:00:00",
  "medico": "Cardiologista"
}
```

### **3. Verificar Consulta na Clínica:**
```http
GET http://localhost:8080/clinica/api/clinica/consultas/111.222.333-44

Headers:
Authorization: Bearer [TOKEN]
```

### **4. Atender Consulta:**
```http
POST http://localhost:8080/clinica/api/clinica/atender-consulta

Headers:
Authorization: Bearer [TOKEN]
Content-Type: application/json

Body:
{
  "cpfPaciente": "111.222.333-44",
  "codigoConsulta": 1,
  "sintomas": ["tosse", "febre", "dor de cabeça"]
}
```

**Response:**
```json
{
  "atendimentoId": 1,
  "possiveisDoencas": ["Gripe", "COVID-19"],
  "tratamentosSugeridos": ["Repouso", "Antitérmico"],
  "procedimentosNecessarios": ["Raio-X"],
  "mensagem": "Atendimento realizado com sucesso"
}
```

---

```

### **RabbitMQ:**

Acesse: http://localhost:15673  
Login: `guest` / `guest`

Verifique as filas:
- `consulta.queue`
- `exame.queue`
- `procedimento.queue`

---

## Comandos Úteis

### **Ver logs:**
```bash
# Todos os serviços
docker-compose logs -f

# Serviço específico
docker-compose logs -f agendamento-service
```

### **Reiniciar serviço:**
```bash
docker-compose restart agendamento-service
```

### **Parar tudo:**
```bash
docker-compose down
```

### **Parar e limpar dados:**
```bash
docker-compose down -v
```

---

## Estrutura do Projeto
```
gerenciamento-hospitalar/
├── agendamento-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── clinica-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── medicina-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── api-gateway/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml
└── README.md
```

---

## 👥 Roles e Permissões

| Endpoint | USUARIO | MEDICO | ADMIN |
|----------|---------|--------|-------|
| Agendar consulta/exame | ✅ | ✅ | ✅ |
| Atender consulta | ❌ | ✅ | ✅ |
| Criar procedimento | ❌ | ✅ | ✅ |
| Listar consultas | ✅ | ✅ | ✅ |
| CRUD de entidades | ❌ | ❌ | ✅ |

---

## Email

Emails de confirmação são enviados após:
- Agendar consulta
- Agendar exame
- Marcar procedimento

**Configuração:** application.properties (Mailtrap para testes)

---

## Tecnologias e Padrões Implementados

- Arquitetura de Microsserviços
- API Gateway Pattern
- Service Discovery (manual via Docker networking)
- Circuit Breaker (retry policies)
- Event-Driven Architecture (RabbitMQ)
- CQRS parcial (leitura/escrita separadas)
- Cache Distribuído (Redis)
- OAuth2 + JWT
- Role-Based Access Control (RBAC)
- Health Checks
- Containerização
- Observabilidade


## Autor

Marcelo - marcelo.bianco.carvalho@gmail.com
