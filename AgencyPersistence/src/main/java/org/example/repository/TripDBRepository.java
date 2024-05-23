package org.example.repository;

import org.example.DBConnection;
import org.example.repository.interfaces.TripRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

@Component
public class TripDBRepository implements TripRepository {
    private Logger logger= LogManager.getLogger();

    private DBConnection dbConnection;
    private Connection connection;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Autowired
    public TripDBRepository(Properties properties) {
        logger.info("Initializing TripRepository with properties: {} ",properties);
        this.dbConnection=new DBConnection(properties);
        this.connection= dbConnection.getConnection();

    }
    @Override
    public Optional<Trip> findOne(Long aLong) {
        logger.traceEntry("Finding trip with id {}" + aLong);
        try(PreparedStatement statement=connection.prepareStatement("SELECT * from trips where id_trip= ?")){
            statement.setLong(1,aLong);
            ResultSet resultSet= statement.executeQuery();
            if (resultSet.next())
            {
                long id=resultSet.getLong("id_trip");
                String place=resultSet.getString("place");
                String transportCompanyName=resultSet.getString("transportCompanyName");
                LocalDateTime time=LocalDateTime.parse(resultSet.getString("departure"),formatter);
                float price = resultSet.getFloat("price");
                int totalSeats = resultSet.getInt("totalSeats");
                Trip trip=new Trip(place,transportCompanyName,time,price,totalSeats);
                trip.setId(id);
                logger.traceExit("trip with id {} found" + aLong);
                return Optional.of(trip);
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error db "+ e);
        }
        logger.traceExit("No trip found with id {}" + aLong);
        return Optional.empty();
    }

    @Override
    public Iterable<Trip> findAll() {
        logger.traceEntry("getting all trips entry");
        ArrayList<Trip> trips=new ArrayList<>();
        try(PreparedStatement statement=connection.prepareStatement("SELECT * FROM trips")){

            ResultSet resultSet= statement.executeQuery();
            logger.info("suntem dupa resultset");
            while (resultSet.next())
            {
                long id= resultSet.getLong("id_trip");
                String place=resultSet.getString("place");
                String transportCompanyName=resultSet.getString("transportCompanyName");

                LocalDateTime time=LocalDateTime.parse(resultSet.getString("departure"),formatter);
                System.out.println(time);
                float price = resultSet.getFloat("price");
                int totalSeats = resultSet.getInt("totalSeats");
                Trip trip=new Trip(place,transportCompanyName,time,price,totalSeats);
                trip.setId(id);
                trips.add(trip);
            }
            logger.traceExit("Got all trips successfully");
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Db failed in findAll trips" + e);
        }
        return trips;
    }

    @Override
    public Optional<Trip> save(Trip entity) {
        logger.traceEntry("Save trip: {}", entity);
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO trips (place, transportCompanyName, departure, price, totalSeats)" +
                        " VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, entity.getPlace());
            statement.setString(2, entity.getTransportCompanyName());
            statement.setString(3, entity.getDeparture().toString().replace('T',' '));
            statement.setFloat(4, entity.getPrice());
            statement.setInt(5, entity.getTotalSeats());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {

                try(PreparedStatement statement1=connection.prepareStatement("select id_trip from trips order by id_trip desc limit 1;")){
                    ResultSet resultSet=statement1.executeQuery();
                    if(resultSet.next()){
                        long id = resultSet.getLong(1);
                        entity.setId(id);
                        logger.traceExit("Trip saved successfully");
                        return Optional.of(entity);
                    }
                }
                catch (Exception e){
                    logger.error("Error retrieving last id", e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving trip: {}", e.getMessage());
            e.printStackTrace();
        }
        logger.traceExit("No trip was saved");
        return Optional.empty();
    }


    @Override
    public Optional<Trip> update(Long integer, Trip entity) {
        logger.traceEntry("Save trip:  {}",entity);
        try(PreparedStatement statement=connection.prepareStatement("UPDATE trips\n" +
                "SET \n" +
                "    place = ?,\n" +
                "    transportCompanyName = ?,\n" +
                "    departure = ?,\n" +
                "    price = ?,\n" +
                "    totalSeats = ?\n" +
                "WHERE\n" +
                "    id_trip = ?;\n")){
            statement.setString(1,entity.getPlace());
            statement.setString(2,entity.getTransportCompanyName());
            statement.setString(3,entity.getDeparture().toString().replace('T',' '));
            statement.setFloat(4,entity.getPrice());
            statement.setInt(5,entity.getTotalSeats());
            statement.setLong(6,integer);
            int rowsAffected= statement.executeUpdate();
            if (rowsAffected>0)
            {
                logger.traceExit("trip saved successfully");

                return Optional.of(entity);
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error db "+ e);
        }
        logger.traceExit("No trip was saved");
        return Optional.empty();
    }

    @Override
    public Optional<Trip> delete(Long integer) {
        logger.traceEntry("Delete trip:  {}",integer);

        try(PreparedStatement statement=connection.prepareStatement("DELETE FROM trips where id_trip = ?;")){
            statement.setLong(1,integer);
            int rowsAffected= statement.executeUpdate();
            if (rowsAffected>0)
            {
                var result=this.findOne(integer);
                logger.traceExit("trip deleted successfully");
                return result;
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error db "+ e);
        }
        logger.traceExit("No trip was saved");
        return Optional.empty();    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterable<Trip> findAllTripPlaceTime(String placeToVisit, LocalDateTime startTime, LocalDateTime endTime) {
       logger.traceEntry("entry the filter");
       ArrayList<Trip> filteredTrips=new ArrayList<>();
            try (PreparedStatement statement=connection.prepareStatement("select * from trips where place=? and departure>?\n" +
                    "and departure <?")) {

                statement.setString(1, placeToVisit);
                statement.setString(2, startTime.toString());
                statement.setString(3, endTime.toString());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    long id = resultSet.getLong("id_trip");
                    String place = resultSet.getString("place");
                    String transportCompanyName = resultSet.getString("transportCompanyName");
                    LocalDateTime time = LocalDateTime.parse(resultSet.getString("departure"), formatter);
                    System.out.println(time);
                    float price = resultSet.getFloat("price");
                    int totalSeats = resultSet.getInt("totalSeats");
                    Trip trip = new Trip(place, transportCompanyName, time, price, totalSeats);
                    trip.setId(id);
                    filteredTrips.add(trip);
                }
                logger.traceExit("Got all FILTERED trips successfully");
            }
            catch (SQLException e)
            {
                logger.error(e);
                System.out.println("db filter error "+ e);
            }
            return filteredTrips;
    }
}
