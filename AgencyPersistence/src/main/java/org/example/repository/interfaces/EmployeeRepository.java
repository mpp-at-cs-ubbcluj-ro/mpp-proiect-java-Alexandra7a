package org.example.repository.interfaces;

import org.example.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends Repository<Long, Employee> {
    public Optional<Employee> findOnebyUsername(String user);
}
