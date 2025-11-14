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

        // Request duplicado
        StudentRequestData req = new StudentRequestData();
        req.setFullName("Nuevo Usuario");
        req.setEmail("test@example.com");
        req.setBirthDate(LocalDate.of(2000, 10, 10));

        // Validación
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);
    }
/*
    @Test
    @DisplayName("Controlador debe responder 404 para ID inexistente")
    void controllerShouldReturn404ForNonExistentId() throws Exception {

        mockMvc.perform(get("api/students/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }


*/

    @Test
    @DisplayName("Debe desactivar un estudiante sin modificar otros atributos")
    void shouldDeactivateStudent() {

        Student student = new Student();
        student.setFullName("Carlos Perez");
        student.setEmail("carlos@example.com");
        student.setBirthDate(LocalDate.of(2000, 5, 20));
        student.setActive(true);
        repository.save(student);

        service.deactivate(student.getId());

        Student updated = repository.findById(student.getId()).orElseThrow();

        assertThat(updated.getEmail()).isEqualTo(student.getEmail());
        assertThat(updated.getFullName()).isEqualTo(student.getFullName());
        assertThat(updated.getBirthDate()).isEqualTo(student.getBirthDate());
    }

}
/*
    // ----------------------------------------------------------------------
    // PRUEBA 4 — Estadísticas
    // ----------------------------------------------------------------------
    @Test
    @DisplayName("Debe retornar estadísticas correctas")
    void shouldReturnCorrectStats() {

        repository.save(new Student("Ana", "ana@example.com",
                LocalDate.of(2001, 1, 1), true));
        repository.save(new Student("Luis", "luis@example.com",
                LocalDate.of(2002, 2, 2), true));
        repository.save(new Student("Pedro", "pedro@example.com",
                LocalDate.of(2003, 3, 3), false));

        var stats = service.getStats();

        assertThat(stats.total()).isEqualTo(3);
        assertThat(stats.active()).isEqualTo(2);
        assertThat(stats.inactive()).isEqualTo(1);
    }

    // ----------------------------------------------------------------------
    // PRUEBA 5 — Interceptor agrega X-Elapsed-Time
    // ----------------------------------------------------------------------
    @Test
    @DisplayName("Interceptor debe agregar el header X-Elapsed-Time")
    void shouldAddElapsedTimeHeader() throws Exception {

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Elapsed-Time"))
                .andExpect(header().string("X-Elapsed-Time", not(emptyString())));
    }

    }*/