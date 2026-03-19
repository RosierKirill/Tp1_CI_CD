package com.tp1cicd;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@Valid @RequestBody final StudentRequest request) {
        return this.studentStore.add(request.toStudent());
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable final int id) {
        return this.studentStore.findById(id);
    }
}
