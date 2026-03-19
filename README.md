# TP1 CI/CD

Projet initialise en Java avec Spring Boot, JUnit 5 et Checkstyle.

## Prerequis

- Java 17
- Maven 3.9+

## Commandes

- Demarrer le serveur : `mvn spring-boot:run`
- Lancer les tests : `mvn test`
- Lancer le linter : `mvn checkstyle:check`

## Endpoint de verification

- `GET /` retourne `API TP1 CI/CD OK`

## CI

Le workflow GitHub Actions utilise `actions/setup-java` puis execute le linter et les tests.
