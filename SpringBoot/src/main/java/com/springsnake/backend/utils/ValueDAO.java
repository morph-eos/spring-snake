package com.springsnake.backend.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.springsnake.backend.values;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValueDAO {

    // Autowired annotation is used for automatic dependency injection by Spring
    @Autowired
    private final ValueRepository valueRepo;

    // List to hold instances of values
    private List<values> valuesList = new ArrayList<>();

    // Method to retrieve values from the repository and store them in the valuesList
    public void pull() {
        valuesList = valueRepo.findAll();
    }

    // Method to convert valuesList to a list of ValueDTO objects
    public List<ValueDTO> getAll() {
        return valuesList.stream().map(value -> {
            // Mapping values to ValueDTO
            return new ValueDTO(value.getKey(), value.getValue(), value.getLastchange());
        }).collect(Collectors.toList());
    }

    // Method to clear the valuesList
    public void clear() {
        valuesList.clear();
    }

    // Method to save a list of ValueDTO objects to valuesList
    public void saveAll(List<ValueDTO> input) {
        for (ValueDTO value : input) {
            // Creating values objects from ValueDTO and adding them to valuesList
            valuesList.add(new values(value.getKey(), value.getValue()));
        }
    }

    // Method to save all values in valuesList back to the repository
    public void close() {
        valueRepo.saveAll(valuesList);
    }
}