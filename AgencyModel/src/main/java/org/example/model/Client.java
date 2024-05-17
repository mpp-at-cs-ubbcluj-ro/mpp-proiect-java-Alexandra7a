package org.example.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@AttributeOverrides({
        @AttributeOverride(name="id", column = @Column(name="id_client"))
})

@Table(name="clients")

public class Client extends Entitate implements Serializable {

    @Column (name="username")
    private String name;
    @Column(name="birthDay")
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
