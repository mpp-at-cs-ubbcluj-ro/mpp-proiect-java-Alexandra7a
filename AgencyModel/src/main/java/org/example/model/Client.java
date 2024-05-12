package org.example.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Client extends Entity<Long> {
    private String name;
    private LocalDate birthDate;

    public Client(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
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
