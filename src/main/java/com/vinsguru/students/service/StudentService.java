package com.vinsguru.students.service;

import com.vinsguru.students.dto.StudentRequest;
import com.vinsguru.students.dto.StudentResponse;
import com.vinsguru.students.entity.Student;
import com.vinsguru.students.exceptions.StudentNotFoundException;
import com.vinsguru.students.mapper.StudentMapper;
import com.vinsguru.students.repository.StudentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository repository;
    private final StudentMapper mapper;

    public StudentService(StudentRepository repository, StudentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public StudentResponse create(StudentRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = getOrThrow(id);
        student.update(request.name().trim(), request.age(), request.className().trim());
        return mapper.toResponse(student);
    }

    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Student getOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }
}
