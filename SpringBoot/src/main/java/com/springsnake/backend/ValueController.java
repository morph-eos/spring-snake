package com.springsnake.backend;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.springsnake.backend.utils.ValueDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Annotation to indicate that this class is a Spring MVC controller
@RequestMapping("/api")
@RestController
@AllArgsConstructor
@Validated
@Slf4j
@CrossOrigin(origins = "*") // Configure this properly in production
public class ValueController {

    // Message for a value not found
    private final String notfound = "The value has not been found";

    // Service that handles business logic for values
    private final ValueService service;

    // Endpoint to retrieve a value by key
    @GetMapping("/get")
    public ResponseEntity<Object> get(@RequestParam("key") @NotBlank String key) {
        log.info("Getting value for key: {}", key);
        // Check if the value is not found
        if (service.get(key).toString() == notfound) {
            log.warn("Value not found for key: {}", key);
            return new ResponseEntity<>(notfound, HttpStatus.NOT_FOUND);
        }
        // Return the value with a success status
        log.info("Successfully retrieved value for key: {}", key);
        return new ResponseEntity<>(service.get(key), HttpStatus.OK);
    }

    // Endpoint to retrieve a full value by key
    @GetMapping("/getfull")
    public ResponseEntity<ValueDTO> getFull(@RequestParam("key") String key) {
        // Check if the value is not found
        if (service.get(key).toString() == notfound) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        // Return the value with a success status
        return new ResponseEntity<>(service.getFull(key), HttpStatus.OK);
    }
    
    // Endpoint to retrieve all values
    @GetMapping("/getall")
    public ResponseEntity<List<ValueDTO>> getAll() {
        // Return all values with a success status
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    // Endpoint to add or update a value
    @PutMapping("/put")
    public ResponseEntity<String> put(@Valid @RequestBody ValueDTO value) {
        log.info("Putting value for key: {}", value.getKey());
        // Check if the value exists
        if (service.get(value.getKey()) == notfound) {
            String result = service.put(value.getKey(), value.getValue());
            log.info("Successfully put value for key: {}", value.getKey());
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        // Return conflict status if the value exists
        log.warn("Value already exists for key: {}", value.getKey());
        return new ResponseEntity<>("This value exists already", HttpStatus.CONFLICT);
    }

    // Endpoint to add or update multiple values
    @PutMapping("/putall")
    public ResponseEntity<String> putAll(@RequestBody List<ValueDTO> inputValues) {
        // Check if any of the values already exist
        for (ValueDTO value : inputValues) {
            if (service.get(value.getKey()) != notfound) {
                return new ResponseEntity<>("One of the values exists already", HttpStatus.CONFLICT);
            }
        }
        // Return success status if all values are added or updated
        return new ResponseEntity<>(service.putAll(inputValues), HttpStatus.OK);
    }

    // Endpoint to update a value by key
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody ValueDTO value) {
        // Check if the value exists
        if (service.get(value.getKey()) != notfound) {
            return new ResponseEntity<String>(service.update(value.getKey(), value.getValue()), HttpStatus.OK);
        }
        // Return not found status if the value does not exist
        return new ResponseEntity<String>(notfound, HttpStatus.NOT_FOUND);
    }

    // Endpoint to delete a value by key
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("key") String key) {
        // Check if the value exists
        if (service.get(key).toString() != notfound) {
            return new ResponseEntity<>(service.delete(key), HttpStatus.OK);
        }
        // Return not found status if the value does not exist
        return new ResponseEntity<>(notfound, HttpStatus.NOT_FOUND);
    }

    // Endpoint to delete all values
    @DeleteMapping("/deleteall")
    public ResponseEntity<String> deleteAll() {
        return new ResponseEntity<>(service.deleteAll(), HttpStatus.OK);
    }
    
}