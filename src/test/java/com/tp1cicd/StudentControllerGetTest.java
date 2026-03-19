package com.tp1cicd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerGetTest {

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
    void shouldReturn200AndArrayForGetStudents() {
        final ResponseEntity<Student[]> response = this.restTemplate.getForEntity(url("/students"), Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(5);
    }

    @Test
    void shouldReturnAllInitialStudents() {
        final ResponseEntity<Student[]> response = this.restTemplate.getForEntity(url("/students"), Student[].class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
            .extracting(Student::email)
            .containsExactly(
                "alice.martin@example.com",
                "nora.petit@example.com",
                "lina.durand@example.com",
                "hugo.bernard@example.com",
                "yanis.robert@example.com"
            );
    }

    @Test
    void shouldReturnMatchingStudentForValidId() {
        final ResponseEntity<Student> response = this.restTemplate.getForEntity(url("/students/1"), Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1);
        assertThat(response.getBody().firstName()).isEqualTo("Alice");
    }

    @Test
    void shouldReturn404ForUnknownId() {
        final ResponseEntity<Map> response = this.restTemplate.getForEntity(url("/students/999"), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsKey("error");
    }

    @Test
    void shouldReturn400ForInvalidId() {
        final ResponseEntity<Map> response = this.restTemplate.getForEntity(url("/students/abc"), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "L'id doit etre un nombre valide");
    }

    private String url(final String path) {
        return "http://localhost:" + this.port + path;
    }
}
