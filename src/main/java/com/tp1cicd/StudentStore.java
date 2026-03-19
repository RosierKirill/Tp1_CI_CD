package com.tp1cicd;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentStore {

    private final Validator validator;
    private final List<Student> students = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public StudentStore(final Validator validator) {
        this.validator = validator;
        reset();
    }

    public synchronized List<Student> findAll() {
        return List.copyOf(this.students);
    }

    public synchronized Student add(final Student student) {
        validate(student);

        if (emailExists(student.email())) {
            throw new IllegalArgumentException("L'email doit etre unique");
        }

        final Student storedStudent = new Student(
            this.nextId.getAndIncrement(),
            student.firstName(),
            student.lastName(),
            student.email(),
            student.grade(),
            student.field()
        );
        this.students.add(storedStudent);
        return storedStudent;
    }

    public synchronized void reset() {
        this.students.clear();
        this.nextId.set(1);

        add(new Student(null, "Alice", "Martin", "alice.martin@example.com", 15.5, "informatique"));
        add(new Student(null, "Nora", "Petit", "nora.petit@example.com", 18.0, "math\u00E9matiques"));
        add(new Student(null, "Lina", "Durand", "lina.durand@example.com", 12.75, "physique"));
        add(new Student(null, "Hugo", "Bernard", "hugo.bernard@example.com", 9.5, "chimie"));
        add(new Student(null, "Yanis", "Robert", "yanis.robert@example.com", 19.0, "informatique"));
    }

    private void validate(final Student student) {
        final Set<ConstraintViolation<Student>> violations = this.validator.validate(student);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Les donnees de l'etudiant sont invalides");
        }
    }

    private boolean emailExists(final String email) {
        return this.students.stream()
            .map(Student::email)
            .anyMatch(existingEmail -> existingEmail.equalsIgnoreCase(email));
    }
}
