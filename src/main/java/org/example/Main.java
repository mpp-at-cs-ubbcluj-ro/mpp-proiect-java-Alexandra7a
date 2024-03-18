package org.example;

import org.example.model.Reservation;
import org.example.repository.Repository;
import org.example.repository.ReservationDBRepository;
import org.example.repository.TripDBRepository;
import org.example.repository.TripRepository;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        System.out.println(repository.findOne(55L));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println(repository.findAllTripPlaceTime("Grecia", LocalDateTime.parse("2024-04-31 14:00",formatter),LocalDateTime.parse("2024-11-31 14:00",formatter)));
        Repository res_repo=new ReservationDBRepository(properties);
        Reservation reservation=new Reservation("Maria",
                "12234",4,1,"adian.pop123",1);
       // res_repo.save(reservation);
        }
}