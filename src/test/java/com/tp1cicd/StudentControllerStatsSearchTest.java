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
class StudentControllerStatsSearchTest {

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

    @Test
    void shouldRejectMissingSearchParameter() {
        final ResponseEntity<Map> response = this.restTemplate.getForEntity(url("/students/search"), Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error");
    }

    private String url(final String path) {
        return "http://localhost:" + this.port + path;
    }
}
