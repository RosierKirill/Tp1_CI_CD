package com.tp1cicd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerPostTest {

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
    void shouldReturn201AndCreatedStudentForValidPost() {
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
    void shouldReturn400WhenRequiredFieldIsMissing() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> request = new HttpEntity<>(
            "{\"firstName\":\"Emma\",\"lastName\":\"Moreau\",\"grade\":14.0,\"field\":\"chimie\"}",
            headers
        );

        final ResponseEntity<Map> response = this.restTemplate.postForEntity(url("/students"), request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error");
    }

    @Test
    void shouldReturn400WhenGradeIsInvalid() {
        final StudentRequest request = new StudentRequest(
            "Emma",
            "Moreau",
            "emma.moreau@example.com",
            25.0,
            "chimie"
        );

        final ResponseEntity<Map> response = this.restTemplate.postForEntity(url("/students"), request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("error");
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() {
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

    private String url(final String path) {
        return "http://localhost:" + this.port + path;
    }
}
