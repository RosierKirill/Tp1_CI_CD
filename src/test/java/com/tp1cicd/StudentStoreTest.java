package com.tp1cicd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StudentStoreTest {

    @Autowired
    private StudentStore studentStore;

    @AfterEach
    void resetData() {
        this.studentStore.reset();
    }

    @Test
    void shouldLoadFiveStudentsAtStartup() {
        final List<Student> students = this.studentStore.findAll();

        assertThat(students).hasSize(5);
        assertThat(students)
            .extracting(Student::field)
            .containsExactlyInAnyOrder("informatique", "math\u00E9matiques", "physique", "chimie", "informatique");
    }

    @Test
    void shouldResetStudentsToInitialState() {
        this.studentStore.add(new Student(null, "Emma", "Moreau", "emma.moreau@example.com", 14.0, "chimie"));

        assertThat(this.studentStore.findAll()).hasSize(6);

        this.studentStore.reset();

        assertThat(this.studentStore.findAll()).hasSize(5);
        assertThat(this.studentStore.findAll())
            .extracting(Student::email)
            .doesNotContain("emma.moreau@example.com");
    }
}
