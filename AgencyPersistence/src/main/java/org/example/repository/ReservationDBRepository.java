package org.example.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DBConnection;
import org.example.model.Reservation;
import org.example.repository.interfaces.ReservationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class ReservationDBRepository implements ReservationRepository {

    private DBConnection dbConnection;
    private Connection connection;
    private static final Logger logger=LogManager.getLogger();


    /**Preparing sttaement here...*/
   // private PreparedStatement getAllById;

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

        logger.traceEntry("entry to save reservation");
        try (PreparedStatement statement=connection.prepareStatement("insert into reservations(clientName, phoneNumber, noSeats, id_trip, username_employee, id_client) \n" +
                "values (?,?,?,?,?,?)"))
        {
            statement.setString(1, entity.getClientName());
            statement.setString(2,entity.getPhoneNumber());
            statement.setInt(3,entity.getNoSeats());
            statement.setLong(4,entity.getTrip().getId());
            statement.setString(5,entity.getResponsibleEmployee().getUsername());
            statement.setLong(6,entity.getClient().getId());
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

    @Override
    public int getAllReservationsAt(Long id) {
        logger.traceEntry("Entered the getAllReservationsAt method ");
        int result=0;
        try (PreparedStatement statement=connection.prepareStatement("select sum(noSeats) from reservations group by id_trip having id_trip=?;"))
        {
            statement.setLong(1,id);
            ResultSet resultSet= statement.executeQuery();
            logger.trace("The result set gave: {}",resultSet);
            result = resultSet.getInt(1);
        }
        catch (SQLException e)
        {

            System.out.println(e.getMessage());
        }
        return result;
    }
}
