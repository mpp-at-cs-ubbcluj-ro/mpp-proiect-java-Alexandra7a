package org.example.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;



public class Client extends Entitate implements Serializable {


    private String name;
    private LocalDate birthDate;

    public Client(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Client() {

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
        return  "Client: "+ name;
    }
}
