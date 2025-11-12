package edu.espe.springlab.Service;

import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentRequestData;
import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.service.impl.StudentServiceImpl;
import edu.espe.springlab.web.advice.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(StudentServiceImpl.class) // Importar la implementación real del servicio
public class StudentServiceTest {

    @Autowired
    private StudentServiceImpl service;

    @Autowired
    private StudentRepository repository;

    @Test
    @DisplayName("Debe lanzar excepción si se intenta registrar un email duplicado")
    void shouldNotAllowDuplicateEmails() {
        // Crear estudiante existente
        Student existing = new Student();
        existing.setFullName("Existing User");
        existing.setEmail("duplicate@example.com");
        existing.setBirthDate(LocalDate.of(2000, 10, 10));
        existing.setActive(true);

        // Guardar en el repositorio
        repository.save(existing);

        // Crear nuevo request con el mismo email
        StudentRequestData req = new StudentRequestData();
        req.setFullName("New User Duplicate");
        req.setEmail("duplicate@example.com");
        req.setBirthDate(LocalDate.of(2000, 10, 10));

        // Verificar que lance excepción por duplicado
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);
    }
}