package com.tp1cicd;

import org.junit.jupiter.api.BeforeEach;
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
class StudentControllerPutDeleteTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentStore studentStore;

    @BeforeEach
    void resetData() {
        this.studentStore.reset();
    }

    @Test
    void shouldReturn200AndUpdatedStudentForValidPut() {
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
        assertThat(response.getBody().grade()).isEqualTo(16.0);
    }

    @Test
    void shouldReturn404ForPutOnUnknownId() {
        final StudentRequest request = new StudentRequest(
            "Alice",
            "Martin",
            "alice.updated@example.com",
            16.0,
            "informatique"
        );

        final ResponseEntity<Map> response = this.restTemplate.exchange(
            url("/students/999"),
            HttpMethod.PUT,
            new HttpEntity<>(request),
            Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsKey("error");
    }

    @Test
    void shouldReturn200ForDeleteOnValidId() {
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
    void shouldReturn404ForDeleteOnUnknownId() {
        final ResponseEntity<Map> response = this.restTemplate.exchange(
            url("/students/999"),
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsKey("error");
    }

    private String url(final String path) {
        return "http://localhost:" + this.port + path;
    }
}
