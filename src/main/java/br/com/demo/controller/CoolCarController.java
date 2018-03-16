package br.com.demo.controller;

import br.com.demo.domain.Car;
import br.com.demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class CoolCarController {

    @Autowired
    private CarRepository repository;

    public CoolCarController(CarRepository repository) {
        this.repository = repository;
    }


    @GetMapping(path="/cool-cars")
    @CrossOrigin(origins = "http://localhost:4200")
    public Collection<Car> coolCars() {
        return repository.findAll().stream()
                .filter(this::isCool)
                .collect(Collectors.toList());
    }

    @GetMapping(path="cars/{id}")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4401"})
    public @ResponseBody ResponseEntity<Car> getCar(@PathVariable Long id) {
        return new ResponseEntity<Car>(repository.findById(id).get(), HttpStatus.OK);
    }

    @PostMapping(path = "/cars", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Car> saveCar(@RequestBody Car car) {
        return new ResponseEntity<Car>(repository.save(car), HttpStatus.OK);
    }

    @DeleteMapping(path="cars/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public @ResponseBody ResponseEntity<String> removeCar(@PathVariable Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            new ResponseEntity<String>("DELETE Response", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("DELETE Response", HttpStatus.OK);
    }

    private boolean isCool(Car car) {
        return !car.getName().equals("AMC Gremlin") &&
                !car.getName().equals("Triumph Stag") &&
                !car.getName().equals("Ford Pinto") &&
                !car.getName().equals("Yugo GV");
    }
}

