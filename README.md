# 📋 ToDo App - Sistema Completo de Gerenciamento de Tarefas

##  Screenshots do Sistema

### LOGIN
<img width="1901" height="1076" alt="Screenshot 2025-08-20 191059" src="https://github.com/user-attachments/assets/8d1f4825-de17-493c-be80-1cf7a38cf855" />

### REGISTER
<img width="1911" height="1072" alt="Screenshot 2025-08-20 191107" src="https://github.com/user-attachments/assets/ec2c4f6e-5ee1-43d9-95a0-24aa15b8c36e" />

### DASHBOARD USER TASKS
<img width="1911" height="1072" alt="Screenshot 2025-08-20 191157" src="https://github.com/user-attachments/assets/7f20deb0-5aea-46cb-9d10-509465df3eca" />

### CREATE TASK
<img width="1664" height="901" alt="Screenshot 2025-08-20 194920" src="https://github.com/user-attachments/assets/c03bdbe0-53c3-4825-bcf0-8415bc3db709" />

### SETTINGS
<img width="1913" height="1067" alt="Screenshot 2025-08-20 191208" src="https://github.com/user-attachments/assets/387b37eb-d952-4b8c-8cd7-a2a9f1812e69" />

### USER MANAGEMENT
<img width="1913" height="1067" alt="Screenshot 2025-08-20 191459" src="https://github.com/user-attachments/assets/6b7d0143-8f6b-4253-b97d-813d57a80e3a" />

### TASK MANAGEMENT OF ALL USERS
<img width="1912" height="1067" alt="Screenshot 2025-08-20 191551" src="https://github.com/user-attachments/assets/f969dbb1-1ed7-4e6d-8a0a-e43db75d352d" />

---


## 📊 Diagrama de Caso de Uso
<img width="955" height="1361" alt="Todo-APP drawio" src="https://github.com/user-attachments/assets/180e2074-72bf-4a67-8afd-4c76eae6df85" />


---

## ✨ Funcionalidades

### 🔐 Sistema de Autenticação
- ✅ Registro e login de usuários  
- ✅ Autenticação JWT segura  
- ✅ Logout com token invalidation  
- ✅ Proteção de rotas baseada em roles  

### 📊 Dashboard Administrativo
- ✅ Visão geral do sistema (gráficos)  
- ✅ Gerenciamento completo de usuários (CRUD)  
- ✅ Visualização de todas as tarefas do sistema  
- ✅ Filtros avançados por status, prioridade e usuário  

### ✅ Gerenciamento de Tarefas
- ✅ Criação, edição e exclusão de tarefas  
- ✅ Sistema de prioridades (Baixa, Média, Alta)  
- ✅ Filtros por status ( Em Progresso, Concluída)  
- ✅ Busca por título ou descrição  
- ✅ Atribuição de tarefas a usuários específicos (para admins)  

### 💻 Interface Responsiva
- ✅ Design adaptável para desktop, tablet e mobile  
- ✅ Navegação intuitiva com sidebar  
- ✅ Modais para ações sem sair da página atual  

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21**  
- **Spring Boot 3.5.4**  
- **Spring Security**  
- **JWT (JSON Web Tokens)**  
- **Spring Data JPA**  
- **MySQL 8.0**  
- **Lombok**  
- **Maven**  
- **Docker**
- **MapStruct**

### Frontend
- **HTML5 / CSS3 / JavaScript**  
- **Font Awesome**  
- **LocalStorage** (para tokens)  

### Ferramentas de Desenvolvimento
- **IntelliJ IDEA** (backend)  
- **VS Code** (frontend)  
- **Postman** (testes de API)  
- **Git & GitHub**  
- **Docker Compose**  

---

## 🚀 Como Executar o Projeto

### 📌 Pré-requisitos
- Java 21+  
- MySQL+  
- Maven (ou Maven wrapper incluso)  
- Node.js (opcional, para servir o frontend)  
- Docker e Docker Compose (opcional)  

### Método 1: Execução Tradicional (sem Docker)
1. Configure o MySQL e crie o banco `todo_app`  
2. Ajuste o `application.properties` no backend  
3. Execute o backend com Maven (`./mvnw spring-boot:run`)  
4. Sirva o frontend com Live Server, Python ou Node.js  

### Método 2: Execução com Docker (recomendado)
```bash
docker-compose up -d
