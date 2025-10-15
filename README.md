# 🛒 Catálogo de Produtos – Projeto Java Spring Boot

Projeto desenvolvido como parte do **bootcamp Java de Elite (Educ360 / Foursys)**, aplicando **arquitetura hexagonal (Ports & Adapters)**, boas práticas de programação, testes e documentação de API.

---

## ✨ Funcionalidades

- **Adicionar produtos:** `POST /produtos`  
- **Buscar produto por ID:** `GET /produtos/{id}`  
- **Listar produtos com filtros e paginação:** `GET /produtos?ativo=true&nome=...`  
- **Atualizar produtos:** `PUT /produtos/{id}`  
- **Exclusão lógica de produtos:** `DELETE /produtos/{id}` (produto fica inativo)

---

## 📋 Regras de negócio

- SKU é obrigatório, único e não pode ser alterado  
- Preço não pode ser negativo  
- Quantidade não pode ser negativa  
- Nome é obrigatório (3 a 120 caracteres)  
- Exclusão é lógica (produto fica inativo)  
- Produtos inativos não aparecem em listagens padrão  
- Atualização de preço atualiza automaticamente a data de modificação  
- ID e SKU não podem ser alterados após criação

---

## 🛠️ Tecnologias utilizadas

- **Java 17**  
- **Spring Boot**  
- **JDBC** (conexão direta com PostgreSQL)   
- **JUnit & Mockito** (testes unitários)  
- **Swagger (OpenAPI)** – documentação interativa da API  
- **Git/GitHub**  
- Princípios de **Clean Code** e **SOLID**  
- Padrões de projeto: **Facade** e **Strategy**

---

## 🏗️ Arquitetura

O projeto segue **arquitetura hexagonal (Ports & Adapters)**:  

- **Domínio:** entidades e regras de negócio  
- **Aplicação:** casos de uso, serviços e validações  
- **Infraestrutura:** implementação de adaptadores para banco, API REST, testes

---

## ▶️ Como rodar

1. Clone o projeto:  
```bash
git clone https://github.com/LidianeMendess/banco-digital-api.git
```
  2. Configure o banco PostgreSQL no application.properties

 3. Execute a aplicação:

    CatalogodeprodutosApplication.main()
    
4. Teste os endpoints:

   - Pelo Swagger: (`http://localhost:8080/swagger-ui/index.html`)

   - Pelo Postman (`postman/CatalogoProdutos.postman_collection.json`)
