# API de categorias e produtos

API REST em camadas com Spring Boot, Spring Data JPA, H2, validação, Spring Security e documentação OpenAPI.

## Requisitos

- Java 17 ou superior
- Maven 3.6 ou superior

## Executar e testar

```shell
mvn spring-boot:run
mvn test
```

Por padrão, a API usa autenticação HTTP Basic com usuário `admin` e senha `admin123`. Configure `APP_USER` e `APP_PASSWORD` no ambiente antes de executar fora de um ambiente local. A API fica disponível em `http://localhost:8080`.

O console do H2 fica em `http://localhost:8080/h2-console`, com JDBC URL `jdbc:h2:mem:catalogo`, usuário `sa` e senha vazia. As credenciais e a URL podem ser substituídas pelas variáveis `DATABASE_USERNAME`, `DATABASE_PASSWORD` e `DATABASE_URL`.

A documentação interativa está em `http://localhost:8080/swagger-ui.html`.

## Endpoints

Os endpoints `/api/categorias` e `/api/produtos` exigem autenticação Basic.

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/categorias` | Lista categorias |
| GET | `/api/categorias/{id}` | Busca categoria |
| POST | `/api/categorias` | Cria categoria (`{"nome":"Livros"}`) |
| PUT | `/api/categorias/{id}` | Atualiza categoria |
| DELETE | `/api/categorias/{id}` | Remove categoria |
| GET | `/api/produtos` | Lista produtos |
| GET | `/api/produtos/{id}` | Busca produto |
| POST | `/api/produtos` | Cria produto (`{"nome":"Livro","preco":39.90,"categoriaId":1}`) |
| PUT | `/api/produtos/{id}` | Atualiza produto |
| DELETE | `/api/produtos/{id}` | Remove produto |

O DTO de produto retorna `categoriaId` e `categoriaNome`; na criação e atualização, informe `categoriaId`.
