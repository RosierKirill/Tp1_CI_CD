package com.tp1cicd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentStore studentStore;

    @AfterEach
    void resetData() {
        this.studentStore.reset();
    }

    @Test
    void shouldGetAllStudents() {
        final ResponseEntity<Student[]> response = this.restTemplate.getForEntity(url("/students"), Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(5);
    }

    @Test
    void shouldGetOneStudentById() {
        final ResponseEntity<Student> response = this.restTemplate.getForEntity(url("/students/1"), Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1);
    }

    @Test
    void shouldReturnNotFoundWhenStudentDoesNotExist() {
        final ResponseEntity<Map> response = this.restTemplate.getForEntity(url("/students/999"), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsKey("error");
    }

    @Test
    void shouldCreateStudent() {
        final StudentRequest request = new StudentRequest(
            "Emma",
            "Moreau",
            "emma.moreau@example.com",
            14.0,
            "chimie"
        );

        final ResponseEntity<Student> response = this.restTemplate.postForEntity(url("/students"), request, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo("emma.moreau@example.com");
    }

    @Test
    void shouldRejectDuplicateEmail() {
        final StudentRequest request = new StudentRequest(
            "Emma",
            "Moreau",
            "alice.martin@example.com",
            14.0,
            "chimie"
        );

        final ResponseEntity<Map> response = this.restTemplate.postForEntity(url("/students"), request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsKey("error");
    }

    @Test
    void shouldUpdateStudent() {
        final StudentRequest request = new StudentRequest(
            "Alice",
            "Martin",
            "alice.updated@example.com",
            16.0,
            "informatique"
        );

        final ResponseEntity<Student> response = this.restTemplate.exchange(
            url("/students/1"),
            HttpMethod.PUT,
            new HttpEntity<>(request),
            Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo("alice.updated@example.com");
    }

    @Test
    void shouldDeleteStudent() {
        final ResponseEntity<Map> response = this.restTemplate.exchange(
            url("/students/1"),
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("message", "Etudiant supprime avec succes");
    }

    @Test
    void shouldReturnStats() {
        final ResponseEntity<Map> response = this.restTemplate.getForEntity(url("/students/stats"), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKeys("totalStudents", "averageGrade", "studentsByField", "bestStudent");
    }

    @Test
    void shouldSearchStudents() {
        final ResponseEntity<Student[]> response = this.restTemplate.getForEntity(url("/students/search?q=ali"), Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].firstName()).isEqualTo("Alice");
    }

    @Test
    void shouldRejectEmptySearch() {
        final ResponseEntity<Map> response = this.restTemplate.getForEntity(url("/students/search?q="), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error");
    }

    private String url(final String path) {
        return "http://localhost:" + this.port + path;
    }
}
