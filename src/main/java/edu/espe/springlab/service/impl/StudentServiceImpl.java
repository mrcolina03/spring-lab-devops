package edu.espe.springlab.service.impl;


import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentRequestData;
import edu.espe.springlab.dto.StudentResponse;
import edu.espe.springlab.dto.StudentUpdate;
import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.service.StudentService;
import edu.espe.springlab.web.advice.ConflictException;
import edu.espe.springlab.web.advice.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;

    public StudentServiceImpl(StudentRepository repo) {
        this.repo = repo;
    }

    @Override
    public StudentResponse create(StudentRequestData request) {
        if(repo.existsByEmail(request.getEmail())){
            throw new ConflictException("El email ya está registrado");
        }
        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setBirthDate(request.getBirthDate());
        student.setActive(true);

        Student saved = repo.save(student);
        return toResponse(saved);
    }

    @Override
    public StudentResponse getById(Long id) {
        Student student = repo.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
        return toResponse(student);
    }

    @Override
    public List<StudentResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public StudentResponse deactivate(Long id) {
        Student student = repo.findById(id).orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
        student.setActive(false);
        return toResponse(repo.save(student));
    }

    @Override
    public Page<Student> searchByFullName(String fullName, Pageable pageable) {
        return repo.findByFullNameContainingIgnoreCase(fullName, pageable);
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentUpdate updateDTO) {
        Student student = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante No Encontrado"));

        if (updateDTO.getFullName() != null) {
            student.setFullName(updateDTO.getFullName());
        }
        if (updateDTO.getEmail() != null) {

            if (repo.existsByEmail(updateDTO.getEmail()) &&
                    !student.getEmail().equals(updateDTO.getEmail())) {
                throw new ConflictException("Ese correo ya fue registrado");
            }
            student.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getBirthDate() != null) {
            student.setBirthDate(updateDTO.getBirthDate());
        }
        if (updateDTO.getActive() != null) {
            student.setActive(updateDTO.getActive());
        }

        repo.save(student);

        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFullName(student.getFullName());
        response.setEmail(student.getEmail());
        response.setBirthDate(student.getBirthDate());
        response.setActive(student.getActive());

        return response;
    }


    private StudentResponse toResponse(Student student){
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFullName(student.getFullName());
        response.setEmail(student.getEmail());
        response.setBirthDate(student.getBirthDate());
        response.setActive(student.getActive());
        return response;
    }
}
