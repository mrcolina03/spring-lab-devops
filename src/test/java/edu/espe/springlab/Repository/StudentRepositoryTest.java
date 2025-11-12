package edu.espe.springlab.Repository;

import edu.espe.springlab.domain.Student;
import edu.espe.springlab.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void shouldFindStudentByEmail() {
        Student student = new Student();
        student.setFullName("Test User");
        student.setEmail("testuser@example.com");
        student.setBirthDate(LocalDate.of(2000, 1, 1));
        student.setActive(true);

        studentRepository.save(student);

        var result = studentRepository.findByEmail("testuser@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("Test User");

    }
}
