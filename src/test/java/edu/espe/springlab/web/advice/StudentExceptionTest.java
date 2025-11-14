package edu.espe.springlab.web.advice;


import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.service.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(StudentServiceImpl.class) // Importar la implementación real del servicio

public class StudentExceptionTest {
    @Autowired
    private StudentServiceImpl service;

    @Autowired
    private StudentRepository repository;



}
