package com.vinsguru.students.mapper;

import com.vinsguru.students.dto.StudentRequest;
import com.vinsguru.students.dto.StudentResponse;
import com.vinsguru.students.entity.Student;

public final class StudentMapper {

    private StudentMapper() {
    }

    public static Student toEntity(StudentRequest request) {
        return new Student(request.name().trim(), request.age(), request.className().trim());
    }

    public static StudentResponse toResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getAge(), student.getClassName());
    }
}
