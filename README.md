# TP1 CI/CD

Projet initialise en Java avec Spring Boot, JUnit 5 et Checkstyle.

Les donnees sont stockees en memoire et reinitialisees a chaque redemarrage.

## Prerequis

- Java 17
- Maven 3.9+

## Commandes

- Demarrer le serveur : `mvn spring-boot:run`
- Lancer les tests : `mvn test`
- Lancer le linter : `mvn checkstyle:check`

## Endpoint de verification

- `GET /` retourne `API TP1 CI/CD OK`
- `GET /students` retourne la liste des etudiants
- `GET /students/{id}` retourne un etudiant
- `POST /students` cree un etudiant
- `PUT /students/{id}` met a jour un etudiant
- `DELETE /students/{id}` supprime un etudiant
- `GET /students/stats` retourne les statistiques
- `GET /students/search?q=...` recherche par nom ou prenom

## Donnees initiales

Le stockage en memoire est pre-rempli avec 5 etudiants de test, avec une fonction `reset()` dans le service `StudentStore`.

## CI

Le workflow GitHub Actions utilise `actions/setup-java` puis execute le linter et les tests.
