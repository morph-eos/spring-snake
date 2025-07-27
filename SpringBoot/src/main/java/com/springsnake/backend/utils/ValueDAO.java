package com.springsnake.backend.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.springsnake.backend.values;

/**
 * Data Access Object for Spring Snake Value operations
 * 
 * This class provides a convenient interface for batch operations on key-value pairs.
 * It acts as a buffer between the service layer and the repository, allowing for
 * efficient bulk operations and data manipulation before persisting to the database.
 * 
 * The ValueDAO maintains an internal list of values that can be manipulated in memory
 * before being committed to the database, making it ideal for batch processing scenarios.
 * 
 * @author M04ph3u2
 * @version 2.0
 * @since 1.0
 */
public class ValueDAO {

    /**
     * Repository interface for database operations
     */
    @Autowired
    private final ValueRepository valueRepo;

    /**
     * Internal list to hold value instances for batch operations
     */
    private List<values> valuesList;

    /**
     * Constructor that initializes the ValueDAO with a repository.
     * 
     * This constructor properly initializes the internal valuesList to prevent
     * NullPointerException issues during operations.
     * 
     * @param valueRepo The ValueRepository instance for database operations
     */
    public ValueDAO(ValueRepository valueRepo) {
        this.valueRepo = valueRepo;
        this.valuesList = new ArrayList<>();
    }

    /**
     * Retrieve all values from the repository and load them into memory.
     * 
     * This method fetches all stored values from the database and populates
     * the internal valuesList for further operations.
     */
    public void pull() {
        valuesList = valueRepo.findAll();
    }

    /**
     * Convert internal valuesList to DTOs for client consumption.
     * 
     * This method transforms the internal values entities into ValueDTO objects
     * suitable for API responses, including key, value, and timestamp information.
     * 
     * @return List of ValueDTO objects representing all values in memory
     */
    public List<ValueDTO> getAll() {
        return valuesList.stream().map(value -> {
            // Mapping values to ValueDTO
            return new ValueDTO(value.getKey(), value.getValue(), value.getLastchange());
        }).collect(Collectors.toList());
    }

    /**
     * Clear all values from the internal memory list.
     * 
     * This method removes all values from the internal valuesList without
     * affecting the database. Use close() to persist changes to the repository.
     */
    public void clear() {
        valuesList.clear();
    }

    /**
     * Add multiple ValueDTO objects to the internal list.
     * 
     * This method converts ValueDTO objects to values entities and adds them
     * to the internal valuesList for batch processing.
     * 
     * @param input List of ValueDTO objects to be added to the internal list
     */
    public void saveAll(List<ValueDTO> input) {
        for (ValueDTO value : input) {
            // Creating values objects from ValueDTO and adding them to valuesList
            valuesList.add(new values(value.getKey(), value.getValue()));
        }
    }

    /**
     * Persist all values from internal list to the database.
     * 
     * This method commits all values stored in the internal valuesList
     * to the database repository. This is typically called after batch
     * operations to save changes permanently.
     */
    public void close() {
        valueRepo.saveAll(valuesList);
    }
}