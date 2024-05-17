package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="employees")
public class Employee extends Entitate {

    @Id
    @Column (name = "username",unique = true)
    private String username;
    @Column(name="pass")
    private String password;

    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Employee() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
