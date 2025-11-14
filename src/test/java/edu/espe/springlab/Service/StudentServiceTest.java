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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;

@DataJpaTest
@Import(StudentServiceImpl.class)
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

        Student existing = new Student();
        existing.setFullName("Existing User");
        existing.setEmail("test@example.com");
        existing.setBirthDate(LocalDate.of(2000, 10, 10));
        existing.setActive(true);

        repository.save(existing);

        StudentRequestData req = new StudentRequestData();
        req.setFullName("Nuevo Usuario");
        req.setEmail("test@example.com");
        req.setBirthDate(LocalDate.of(2001, 10, 10));

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);
    }

/*
    @Test
    @DisplayName("Debe lanzar NotFoundException al consultar un ID inexistente")
    void shouldThrowNotFoundExceptionWhenIdDoesNotExist() {

        Long nonExistentId = 9999L;

        assertThatThrownBy(() -> service.getById(nonExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("no encontrado")
                .hasMessageContaining("9999");
    }

    @Test
    @DisplayName("Controlador debe responder 404 para ID inexistente")
    void controllerShouldReturn404ForNonExistentId() throws Exception {
        mockMvc.perform(get("/students/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }


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

        assertThat(updated.isActive()).isFalse();
        assertThat(updated.getEmail()).isEqualTo(student.getEmail());
        assertThat(updated.getFullName()).isEqualTo(student.getFullName());
        assertThat(updated.getBirthDate()).isEqualTo(student.getBirthDate());
    }


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


    @Test
    @DisplayName("Interceptor debe agregar el header X-Elapsed-Time")
    void shouldAddElapsedTimeHeader() throws Exception {

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Elapsed-Time"))
                .andExpect(header().string("X-Elapsed-Time", not(emptyString())));
    }


    @Test
    @DisplayName("Debe buscar estudiantes por nombre parcial")
    void shouldSearchByPartialName() {

        repository.save(new Student("Ana", "ana@example.com", LocalDate.now(), true));
        repository.save(new Student("Andrea", "andrea@example.com", LocalDate.now(), true));
        repository.save(new Student("Juan", "juan@example.com", LocalDate.now(), true));

        List<Student> result = service.searchByName("an");

        assertThat(result)
                .extracting(Student::getFullName)
                .contains("Ana", "Andrea")
                .doesNotContain("Juan");
    }*/
}
