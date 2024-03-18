package org.example.repository;

import org.example.model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReservationDBRepository implements Repository<Long, Reservation >{

    private DBConnection dbConnection;
    private Connection connection;
    private static final Logger logger=LogManager.getLogger();

    public ReservationDBRepository(Properties properties) {
        logger.traceEntry("Constructor entry with properties {}",properties);
        this.dbConnection=new DBConnection(properties);
        this.connection= dbConnection.getConnection();
    }

    @Override
    public Optional<Reservation> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Reservation> findAll() {
        return null;
    }

    @Override
    public Optional<Reservation> save(Reservation entity) {
        ///todo
        logger.traceEntry("entry to save reservation");
        try (PreparedStatement statement=connection.prepareStatement("insert into reservations(clientName, phoneNumber, noSeats, id_trip, username_employee, id_client) \n" +
                "values (?,?,?,?,?,?)"))
        {
            statement.setString(1, entity.getClientName());
            statement.setString(2,entity.getPhoneNumber());
            statement.setInt(3,entity.getNoSeats());
            statement.setInt(4,entity.getTrip());
            statement.setString(5,entity.getResponsibleEmployee());
            statement.setInt(6,entity.getClient());
            int resultSet= statement.executeUpdate();
            if(resultSet !=0)
            {   logger.traceExit("added reservation {}"+entity);
                return Optional.of(entity);}
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("eroare la db reservation "+e);
        }
        logger.traceExit("NO reservation added");
        return Optional.empty();
    }

    @Override
    public Optional<Reservation> update(Long aLong, Reservation entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Reservation> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public int size() {
        return 0;
    }
}
