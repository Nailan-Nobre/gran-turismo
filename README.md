# Gran Turismo - Sistema de Turismo

Repositório da Atividade Avaliativa - Sistema de Gerenciamento de Turismo com CRUD completo.

## 📋 Pré-requisitos

Antes de começar, você precisa ter instalado em sua máquina:

- **Java JDK 24** ou superior ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **MySQL Workbench 8.0.45** ([Download](https://dev.mysql.com/downloads/workbench/))
- **Git** ([Download](https://git-scm.com/downloads))

## 🚀 Como Rodar o Projeto

### 1. Clone o Repositório

Abra o terminal e execute:

```bash
git clone https://github.com/seu-usuario/gran-turismo.git
cd gran-turismo
```

### 2. Configure o Banco de Dados MySQL Workbench

Acesse o MySQL Workbench e crie o banco de dados:

```sql
CREATE DATABASE turismo_db;
```

### 3. Configure a Conexão com o Banco

Edite o arquivo [src/main/resources/META-INF/persistence.xml](src/main/resources/META-INF/persistence.xml) e ajuste as credenciais do MySQL:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/turismo_db"/>
<property name="jakarta.persistence.jdbc.user" value="SEU_USUARIO"/>
<property name="jakarta.persistence.jdbc.password" value="SUA_SENHA"/>
```

### 4. Compile o Projeto

No terminal, na raiz do projeto, execute:

```bash
mvn clean compile
```

### 5. Execute o Programa

Você pode executar o programa de duas formas:

**Opção 1: Via Maven**
```bash
mvn exec:java -Dexec.mainClass="br.edu.ifpi.SistemaTurismoCRUD"
```

**Opção 2: Via Java (após compilar)**
```bash
java -cp "target/classes;C:\Users\SEU_USUARIO\.m2\repository\**\*.jar" br.edu.ifpi.SistemaTurismoCRUD
```

## 📦 Dependências

O projeto utiliza as seguintes dependências (gerenciadas pelo Maven):

- Jakarta Persistence API 3.1.0
- Hibernate Core 6.4.4.Final
- MySQL Connector 8.0.33

## 🏗️ Estrutura do Projeto

```
src/main/java/br/edu/ifpi/
├── dao/              # Camada de acesso a dados
├── factory/          # Factories para criação de objetos
├── Model/            # Entidades do domínio
├── service/          # Lógica de negócio
└── util/             # Utilitários (validação, cores, etc)
```

## 💡 Funcionalidades

- CRUD completo de Clientes
- CRUD completo de Destinos
- CRUD completo de Hospedagens
- CRUD completo de Voos
- Sistema de Reservas
- Múltiplos métodos de pagamento

## 🛠️ Tecnologias Utilizadas

- Java 24
- Maven
- JPA/Hibernate
- MySQL
- Design Patterns (DAO, Factory)

## 👥 Equipe
- [Fernanda Ellen](https://github.com/Fernandaellen002)
- [Fernanda Istéfane](https://github.com/FernandaIstefane)
- [Nailan Nobre](https://github.com/Nailan-Nobre)
- [Roberto Algusto](https://github.com/EtoDoze  )
- [Yan Carlos]()

---

Este projeto foi desenvolvido para fins acadêmicos.
