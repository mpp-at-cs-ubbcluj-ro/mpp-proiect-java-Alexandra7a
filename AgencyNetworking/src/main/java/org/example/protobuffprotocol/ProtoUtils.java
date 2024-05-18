package org.example.protobuffprotocol;

import org.example.model.*;
import org.example.model.dto.ReservationDTO;
import org.example.model.dto.*;
import org.example.model.dto.TripFilterBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;

public class ProtoUtils {

    public static AgencyProtocol.Request createLoginRequest(EmployeeDTO employee1) {
        AgencyProtocol.EmployeeDTO utilizator = AgencyProtocol.EmployeeDTO.newBuilder().setUsername(employee1.getUsername()).setPassword(employee1.getPassword()).build();
        AgencyProtocol.Request request = AgencyProtocol.Request.newBuilder().setType(AgencyProtocol.Request.RequestType.LOGIN).setUserDto(utilizator).build();
        return request;
    }

    public static AgencyProtocol.Request createLogoutRequest(EmployeeDTO employee1) {
        //todo: the create dto statement does not need to be included here
        AgencyProtocol.EmployeeDTO utilizator = AgencyProtocol.EmployeeDTO.newBuilder().setUsername(employee1.getUsername()).setPassword(employee1.getPassword()).build();
        AgencyProtocol.Request request = AgencyProtocol.Request.newBuilder().setType(AgencyProtocol.Request.RequestType.LOGOUT).setUserDto(utilizator).build();
        return request;
    }

    public static AgencyProtocol.Request createGetAllTripsRequest() {
        AgencyProtocol.Request request = AgencyProtocol.Request.newBuilder().setType(AgencyProtocol.Request.RequestType.FIND_ALL_TRIPS).build();
        return request;
    }

    public static AgencyProtocol.Request createGetTripsFilteredRequest(TripFilterBy filterDTO){
        AgencyProtocol.TripFilterBy filter = AgencyProtocol.TripFilterBy.newBuilder()
                .setPlaceToVisit(filterDTO.getPlaceToVisit())
                .setStartTime(filterDTO.getStartTime().toString())
                .setEndTime(filterDTO.getEndTime().toString()).build();
        AgencyProtocol.Request request = AgencyProtocol.Request.newBuilder().setType(AgencyProtocol.Request.RequestType.FILTER_TRIPS).setTripFilterByDataRequest(filter).build();
        return request;
    }

    public static AgencyProtocol.Request createReservationTicketRequest(ReservationDTO reservationDTO){
        AgencyProtocol.Employee employee=AgencyProtocol.Employee.newBuilder().setUsername(reservationDTO.getResponsibleEmployee().getUsername()).setPassword(reservationDTO.getResponsibleEmployee().getPassword()).build();
        AgencyProtocol.Client client=AgencyProtocol.Client.newBuilder().setId(reservationDTO.getClient().getId()).setName(reservationDTO.getClient().getName()).setBirthdate(reservationDTO.getClient().getBirthDate().toString()).build();
        AgencyProtocol.Trip trip=AgencyProtocol.Trip.newBuilder()
                .setId(reservationDTO.getTrip().getId())
                .setPlace(reservationDTO.getTrip().getPlace())
                .setTransportCompanyName(reservationDTO.getTrip().getTransportCompanyName())
                .setDeparture(reservationDTO.getTrip().getDeparture().toString())
                .setPrice(reservationDTO.getTrip().getPrice())
                .setTotalSeats(reservationDTO.getTrip().getTotalSeats()).build();
        System.out.println("PRoto utils save reservation");
        System.out.println(trip);
        System.out.println(client);
        AgencyProtocol.ReservationDTO reservation = AgencyProtocol.ReservationDTO.newBuilder()
                .setClientName(reservationDTO.getClientName())
                .setPhoneNumber(reservationDTO.getPhoneNumber())
                .setNoSeats(reservationDTO.getNoSeats())
                .setClient(client)
                .setResponsibleEmployee(employee)
                .setTrip(trip).build();
        AgencyProtocol.Request request = AgencyProtocol.Request.newBuilder().setType(AgencyProtocol.Request.RequestType.RESERVE_TICKET).setReservationDto(reservation).build();
        return request;
    }

    public static  AgencyProtocol.Request createGetAllReservationsNumberRequest(long id){
        AgencyProtocol.Request request = AgencyProtocol.Request.newBuilder().setType(AgencyProtocol.Request.RequestType.GET_RESERVATIONS).setId(id).build();
        return request;
    }
    public static  AgencyProtocol.Request createFindAllClientsRequest(){

        AgencyProtocol.Request request = AgencyProtocol.Request.newBuilder().setType(AgencyProtocol.Request.RequestType.FIND_ALL_CLIENTS).build();
        return request;
    }




    public static AgencyProtocol.Response createEmptyOkResponse(){
        AgencyProtocol.Response response = AgencyProtocol.Response.newBuilder().setType(AgencyProtocol.Response.ReponseType.OK).build();
        return response;
    }

    public static AgencyProtocol.Response createErrorResponse(String text){
        AgencyProtocol.Response response = AgencyProtocol.Response.newBuilder().setType(AgencyProtocol.Response.ReponseType.ERROR).setError(text).build();
        return response;
    }


    public static AgencyProtocol.Response createUpdateResponse(){
        AgencyProtocol.Response response = AgencyProtocol.Response.newBuilder().setType(AgencyProtocol.Response.ReponseType.UPDATE).build();
        return response;
    }

    public static AgencyProtocol.Response createFindAllTripsResponse(List<TripDTO> trips){

        AgencyProtocol.Response.Builder responseBuilder = AgencyProtocol.Response.newBuilder().setType(AgencyProtocol.Response.ReponseType.OK);
        for(var trip : trips){
            AgencyProtocol.TripDTO tripProto=AgencyProtocol.TripDTO.newBuilder()
                    .setPlace(trip.getPlace())
                    .setTransportCompanyName(trip.getTransportCompanyName())
                    .setDeparture(trip.getDeparture().toString())
                    .setPrice(trip.getPrice())
                    .setTotalSeats(trip.getTotalSeats()).build();
            responseBuilder.addTrips(tripProto);
        }
        return responseBuilder.build();
    }

    public static AgencyProtocol.Response createFindAllFilteredTripsResponse(List<TripDTO> trips){
        AgencyProtocol.Response.Builder responseBuilder = AgencyProtocol.Response.newBuilder().setType(AgencyProtocol.Response.ReponseType.OK);

        for(var trip : trips){
            AgencyProtocol.TripDTO tripProto=AgencyProtocol.TripDTO.newBuilder()
                    .setId(trip.getId())
                    .setPlace(trip.getPlace())
                    .setTransportCompanyName(trip.getTransportCompanyName())
                    .setDeparture(trip.getDeparture().toString())
                    .setPrice(trip.getPrice())
                    .setTotalSeats(trip.getTotalSeats()).build();
            responseBuilder.addTrips(tripProto);
        }
        return responseBuilder.build();
    }

    public static AgencyProtocol.Response createFindAllClientsResponse(List<ClientDTO> clients) {
        AgencyProtocol.Response.Builder responseBuilder = AgencyProtocol.Response.newBuilder().setType(AgencyProtocol.Response.ReponseType.OK);

        for (var client : clients) {
            AgencyProtocol.ClientDTO clientProto = AgencyProtocol.ClientDTO.newBuilder()
                    .setId(client.getId())
                    .setName(client.getName())
                    .setBirthDate(client.getBirthDate().toString()).build();

            responseBuilder.addClients(clientProto);
        }
        return responseBuilder.build();
    }

    public static AgencyProtocol.Response createNumberReservationsResponse(int number){
        AgencyProtocol.Response response= AgencyProtocol.Response.newBuilder().setType(AgencyProtocol.Response.ReponseType.OK).setNo(number).build();
        return response;
    }
    /**
     * to be able to extract the data from a response or a request
     * */

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");

    public static List<TripDTO> getTripsFromResponse(AgencyProtocol.Response response){

        var trips = response.getTripsList();
        List<TripDTO> tripsT = trips.stream()
                .map(trip -> {var newTrip =new TripDTO( trip.getPlace(),
                        trip.getTransportCompanyName(),
                        LocalDateTime.parse(trip.getDeparture(),formatter),
                        trip.getPrice(),
                        trip.getTotalSeats());
                    newTrip.setId(trip.getId());
                    return newTrip;})
                .toList();

        System.out.println("IN CLIENT A AJUNS:::______________________");
        tripsT.forEach(System.out::println);
        return tripsT;
    }
    public static List<ClientDTO> getClientsFromResponse(AgencyProtocol.Response response){
        var clients = response.getClientsList();
        return clients.stream()
                .map(client -> {var newClient =new ClientDTO( client.getName(),
                        LocalDate.parse(client.getBirthDate(),formatter));
                    newClient.setId(client.getId());
                    return newClient;})
                .toList();
    }

    public static int getReservationNumberFromResponse(AgencyProtocol.Response response){
        return response.getNo();
    }

    public static EmployeeDTO getEmployeeDTO(AgencyProtocol.Request request){
        var employeeDTO = request.getUserDto();
        return new EmployeeDTO( employeeDTO.getUsername(), employeeDTO.getPassword());
    }

    public static TripFilterBy getTripFilterByDataFromRequest(AgencyProtocol.Request request){
        var data= request.getTripFilterByDataRequest();
        return new TripFilterBy(data.getPlaceToVisit(),LocalDateTime.parse(data.getStartTime()),LocalDateTime.parse(data.getEndTime()));
    }

    public static ReservationDTO getReservationFromRequest(AgencyProtocol.Request request){
        /*
         * Must transform reservation proto in normal reservation */
        var reservationDTO=request.getReservationDto();

        Employee employee=new Employee(reservationDTO.getResponsibleEmployee().getUsername(),
                reservationDTO.getResponsibleEmployee().getPassword());

        Trip trip=new Trip(reservationDTO.getTrip().getPlace(),
                reservationDTO.getTrip().getTransportCompanyName(),
                LocalDateTime.parse(reservationDTO.getTrip().getDeparture()),
                reservationDTO.getTrip().getPrice(),
                reservationDTO.getTrip().getTotalSeats()
        );
        Client client=new Client(reservationDTO.getClient().getName(),LocalDate.parse(reservationDTO.getClient().getBirthdate()));

        return new ReservationDTO(reservationDTO.getClientName(),
                reservationDTO.getPhoneNumber(),
                reservationDTO.getNoSeats(),
                trip,
                employee,
                client);

    }
}
