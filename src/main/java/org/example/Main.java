package org.example;

import org.example.model.Client;
import org.example.model.Employee;
import org.example.model.Reservation;
import org.example.model.Trip;
import org.example.repository.interfaces.Repository;
import org.example.repository.ReservationDBRepository;
import org.example.repository.TripDBRepository;
import org.example.repository.interfaces.TripRepository;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        System.out.println("a test here ");
        Properties properties=new Properties();
        try {
            properties.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        TripRepository repository=new TripDBRepository(properties);
        System.out.println(repository.findAll());
        //System.out.println(repository.findOne(55L));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println(repository.findAllTripPlaceTime("Grecia", LocalDateTime.parse("2024-04-31 14:00",formatter),LocalDateTime.parse("2024-11-31 14:00",formatter)));
        Repository<Long,Reservation> res_repo=new ReservationDBRepository(properties);

        Optional<Trip> trip=repository.findOne(46L);
        Employee employee=new Employee("adian.pop123","kdshksdhh");
        Client client=new Client("Adi", LocalDate.parse("2000-10-23"));
        System.out.println(LocalDate.parse("2000-10-23"));
        client.setId(1L);



        if (trip.isPresent()) {
            Reservation reservation = new Reservation("Tom",
                    "12234",2,trip.get(),employee,client);
            res_repo.save(reservation);
        }
    }
}