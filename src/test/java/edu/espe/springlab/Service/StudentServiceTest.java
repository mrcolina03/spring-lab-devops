package edu.espe.springlab.Service;

import edu.espe.springlab.service.StudentService;
import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentRequestData;
import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.web.advice.ConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@Import(StudentServiceTest.class)
public class StudentServiceTest {
    @Autowired
    private StudentService service;

    @Autowired
    private StudentRepository repository;

    @Test
    void shouldNotAllowDuplicateEmail() {
        Student existing =  new Student();
        existing.setFullName("Test User");
        existing.setEmail("diplucate@example.com");
        existing.setBirthDate(LocalDate.of(2000, 1, 1));
        existing.setActive(true);

        repository.save(existing);

        StudentRequestData req =  new StudentRequestData();
        req.setFullName("New User Dup");
        req.setEmail("duplicate@example.com");
        req.setBirthDate(LocalDate.of(2000, 1, 1));


        assertThatThrownBy(() -> service.create(req)).isInstanceOf(ConflictException.class);
    }
}