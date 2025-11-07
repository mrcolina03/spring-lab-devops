package edu.espe.springlab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class StudentUpdate {

    @Size(min = 3, max = 120, message = "Ddebe tener entre 3 y 120 caracteres")
    private String fullName;

    @Email(message = "Formato de correo no válido")
    @Size(max = 120, message = "120 carácteres como máximo")
    private String email;

    private LocalDate birthDate;

    private Boolean active;

    public StudentUpdate() {}


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
