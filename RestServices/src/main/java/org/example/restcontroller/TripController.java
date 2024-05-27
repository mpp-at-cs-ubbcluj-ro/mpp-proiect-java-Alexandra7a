package org.example.restcontroller;

import org.example.model.Trip;
import org.example.repository.interfaces.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin  //(Allows cross-origin requests)  used to ensure clients from different web domains can access this backend resource
@RestController // Indicates that this class is a REST controller, which means it will handle HTTP requests and produce JSON responses
@RequestMapping("/agency/trips") // the 'root' from URI which is the base for all requests coming

//the following additions to the 'root' are called REST API Endpoints
public class TripController {

    private static final String template = "Hello, %s!";

    @Autowired //Automatically injects the TripRepository dependency into the controller
    private TripRepository tripRepository;

    @RequestMapping("/greeting") // YOU MAP THE REQUEST TO THIS FUNCTION TO DEAL WITH IT
    public  String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }
    /**
     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
     *     public Iterable<Trip> getAll(@PathVariable Long page){
      *     //to get a page from repo and display them paging manner
     *         return tripRepository.findAll(page);
     *     }
     * */
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Trip> getAll(){
        return tripRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Trip save(@RequestBody Trip trip){

        //Trip newtrip=new Trip(trip.getPlace(),trip.getTransportCompanyName(),trip.getDeparture(),trip.getPrice(),trip.getTotalSeats());
        tripRepository.save(trip);
        return trip;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Trip update(@PathVariable Long id,@RequestBody Trip user) {
        System.out.println("Updating user ...");
        user.setId(id);
        tripRepository.update(id, user);
        return user;

    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id){
        System.out.println("Deleting user ... "+id);
        try {
            tripRepository.delete(id);
            return new ResponseEntity<Trip>(HttpStatus.OK);
        }catch (Exception ex){
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
/*

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){
        System.out.println("Get by id "+id);
        User user=userRepository.findBy(id);
        if (user==null)
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User create(@RequestBody User user){
        userRepository.save(user);
        return user;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public User update(@RequestBody User user) {
        System.out.println("Updating user ...");
        userRepository.update(user.getId(),user);
        return user;

    }
    // @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/{username}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String username){
        System.out.println("Deleting user ... "+username);
        try {
            userRepository.delete(username);
            return new ResponseEntity<User>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping("/{user}/name")
    public String name(@PathVariable String user){
        User result=userRepository.findBy(user);
        System.out.println("Result ..."+result);

        return result.getName();
    }



    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }*/
}
