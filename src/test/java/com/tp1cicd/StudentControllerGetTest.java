package com.tp1cicd;

import org.junit.jupiter.api.AfterEach;
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
    void shouldGetStudentById() {
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
    void shouldReturnBadRequestWhenIdIsInvalid() {
        final ResponseEntity<Map> response = this.restTemplate.getForEntity(url("/students/not-a-number"), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "L'id doit etre un nombre valide");
    }

    private String url(final String path) {
        return "http://localhost:" + this.port + path;
    }
}
