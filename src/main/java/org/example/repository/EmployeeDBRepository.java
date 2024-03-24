package org.example.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Employee;
import org.example.repository.interfaces.EmployeeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class EmployeeDBRepository implements EmployeeRepository {
    private Logger logger= LogManager.getLogger();
    private DBConnection dbConnection;
    private Connection connection;

    public EmployeeDBRepository(Properties properties) {
        logger.info("Initializing Employee with properties: {} ",properties);
        this.dbConnection=new DBConnection(properties);
        this.connection= dbConnection.getConnection();

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

    @Override
    public Optional<Employee> findOnebyUsername(String user) {
        logger.traceEntry("Finding employee with id {}" + user);
        try(PreparedStatement statement=connection.prepareStatement("SELECT * from employees where username= ?")){
            statement.setString(1,user);
            ResultSet resultSet= statement.executeQuery();
            if (resultSet.next())
            {
                String userame=resultSet.getString("username");
                String pass=resultSet.getString("pass");
                Employee employee=new Employee(userame,pass);
                logger.traceExit("trip with id {} found" + user);
                return Optional.of(employee);
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error db "+ e);
        }
        logger.traceExit("No trip found with id {}" + user);
        return Optional.empty();
    }
}
