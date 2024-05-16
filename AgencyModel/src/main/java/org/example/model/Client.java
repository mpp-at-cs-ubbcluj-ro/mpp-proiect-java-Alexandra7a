package org.example.model;

import java.time.LocalDate;
import javax.persistence.*;
@Entity
@Table(name="clients")
@AttributeOverrides({
        @AttributeOverride(name="id", column = @Column(name="id_client"))
})
public class Client extends Entityy {

    @Column (name="username")
    private String name;
    @Column(name="birthDay")
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
