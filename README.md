Catálogo de Produtos - Projeto Java Spring Boot

Este é um projeto de estudo feito em Java com Spring Boot, onde você pode cadastrar, listar, atualizar e desativar produtos de uma loja básica.

Funcionalidades:

* Adicionar produtos (POST /produtos)
* Buscar produto por ID (GET /produtos/{id})
* Listar produtos com filtros e paginação (GET /produtos?ativo=true\&nome=...)
* Atualizar produtos (PUT /produtos/{id})
* Exclusão lógica de produtos (DELETE /produtos/{id}, apenas desativa)

Regras de negócio:

1. SKU é obrigatório, único e não pode ser alterado.
2. Preço não pode ser negativo.
3. Quantidade não pode ser negativa.
4. Nome é obrigatório (3 a 120 caracteres).
5. Exclusão é lógica (produto fica inativo).
6. Produtos inativos não aparecem em listagens padrão.
7. Atualização de preço atualiza automaticamente a data de modificação.
8. ID e SKU não podem ser alterados após criação.

Tecnologias usadas:

* Java 17
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Bean Validation (javax/jakarta.validation)

Como rodar:

1. Clonar o projeto
2. Configurar o banco PostgreSQL no application.properties
3. Rodar a aplicação (método main em CatalogodeprodutosApplication)
4. Testar os endpoints via Postman ou Insomnia

## Testando a API com Postman
Você pode importar a coleção do Postman que está no arquivo `postman/CatalogoProdutos.postman_collection.json` para testar todos os endpoints.
