package edu.espe.springlab.Service;

import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentRequestData;
import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.service.impl.StudentServiceImpl;
import edu.espe.springlab.web.advice.ConflictException;
import edu.espe.springlab.web.advice.NotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentServiceTest {

    @Autowired
    private StudentServiceImpl service;

    @Autowired
    private StudentRepository repository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Debe lanzar excepción si se intenta registrar un email duplicado")
    void shouldNotAllowDuplicateEmails() {

        // Estudiante existente
        Student existing = new Student();
        existing.setFullName("Existing User");
        existing.setEmail("test@example.com");
        existing.setBirthDate(LocalDate.of(2000, 10, 10));
        existing.setActive(true);
        repository.save(existing);

        // Request duplicado . Prueba forzada a fallar
        StudentRequestData req = new StudentRequestData();
        req.setFullName("Nuevo Usuario");
        req.setEmail("testNuevo@example.com");
        req.setBirthDate(LocalDate.of(2000, 10, 10));

        // Validación
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);
    }

}
