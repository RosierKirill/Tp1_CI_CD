package com.tp1cicd;

import java.util.Map;

public record StudentStats(
    int totalStudents,
    double averageGrade,
    Map<String, Long> studentsByField,
    Student bestStudent
) {
}
