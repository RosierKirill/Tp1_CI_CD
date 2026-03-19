package com.tp1cicd;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(final int id) {
        super("Aucun etudiant trouve pour l'id " + id);
    }
}
