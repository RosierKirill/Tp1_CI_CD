package com.tp1cicd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentStore studentStore;

    public StudentController(final StudentStore studentStore) {
        this.studentStore = studentStore;
    }

    @GetMapping
    public List<Student> getStudents() {
        return this.studentStore.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable final int id) {
        return this.studentStore.findById(id);
    }
}
