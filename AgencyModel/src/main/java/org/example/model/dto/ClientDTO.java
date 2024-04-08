package org.example.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class ClientDTO implements Serializable {
    private Long id;

    private String name;
    private LocalDate birthDate;

    public ClientDTO(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    @Override
    public String toString()
    {
        return "id" + id + "Client: "+ name;
    }
}
