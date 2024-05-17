package org.example.repository;

import org.example.HibernateUtils;
import org.example.model.Employee;
import org.example.repository.interfaces.EmployeeRepository;
import org.hibernate.Session;

import java.util.Optional;

public class EmployeeHibernateRepository implements EmployeeRepository {
    @Override
    public Optional<Employee> findOnebyUsername(String user) {
        System.out.println("FIND ONE USER BY NAME");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Employee employee =  session.createSelectionQuery("from Employee where username=:username ", Employee.class)
                    .setParameter("username",user)
                    .getSingleResultOrNull();
            if (user == null){
                return Optional.empty();
            }
            return Optional.of(employee);
        }
    }

    @Override
    public Optional<Employee> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Employee> findAll() {
        return null;
    }

    @Override
    public Optional<Employee> save(Employee entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Employee> update(Long aLong, Employee entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Employee> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public int size() {
        return 0;
    }
}
