package com.tp1cicd;

import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/stats")
    public StudentStats getStats() {
        return this.studentStore.getStats();
    }

    @GetMapping("/search")
    public List<Student> searchStudents(@RequestParam(name = "q") final String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Le parametre q ne doit pas etre vide");
        }
        return this.studentStore.search(query);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable final int id) {
        return this.studentStore.findById(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable final int id, @Valid @RequestBody final StudentRequest request) {
        return this.studentStore.update(id, request.toStudent());
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteStudent(@PathVariable final int id) {
        this.studentStore.delete(id);
        return Map.of("message", "Etudiant supprime avec succes");
    }
}
