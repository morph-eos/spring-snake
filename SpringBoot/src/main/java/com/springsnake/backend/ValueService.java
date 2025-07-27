package com.springsnake.backend;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springsnake.backend.utils.ValueDAO;
import com.springsnake.backend.utils.ValueDTO;
import com.springsnake.backend.utils.ValueRepository;

import lombok.AllArgsConstructor;

/**
 * Service layer for Spring Snake Key-Value Store operations
 * 
 * This service class encapsulates the business logic for managing key-value pairs
 * in the Spring Snake application. It acts as an intermediary between the 
 * ValueController and the data access layer, providing methods for all CRUD 
 * operations with proper error handling and data transformation.
 * 
 * The service utilizes ValueDAO for batch operations and direct repository
 * access for individual operations, ensuring optimal performance and consistency.
 * 
 * @author M04ph3u2
 * @version 2.0
 * @since 1.0
 */
@AllArgsConstructor
@Service
public class ValueService {

    /**
     * Repository interface for direct database operations on values
     */
    @Autowired
    private final ValueRepository valueRepo;

    /**
     * Retrieve a value by its key (value content only).
     * 
     * This method returns only the value content without metadata like timestamps.
     * Use getFull() to retrieve the complete object with metadata.
     * 
     * @param key The key to search for in the database
     * @return The value associated with the key, or error message if not found
     */
    public Object get(String key) {
        // Retrieve the value from the repository
        values value = valueRepo.findByKey(key);
        // Check if the value is not found
        if (value == null) {
            return "The value has not been found";
        } else {
            // Return the value
            return value.getValue();
        }
    }

    /**
     * Retrieve complete value object by key including metadata.
     * 
     * This method returns the full ValueDTO object containing the key, value,
     * and timestamp information. Use get() for value content only.
     * 
     * @param key The key to search for in the database
     * @return ValueDTO object with complete data, or null if not found
     */
    public ValueDTO getFull(String key) {
        // Retrieve the value from the repository
        values value = valueRepo.findByKey(key);
        // Check if the value is not found
        if (value == null) {
            return null;
        } else {
            // Return the value
            return new ValueDTO(value.getKey(), value.getValue(), value.getLastchange());
        }
    }

    /**
     * Retrieve all key-value pairs from the database.
     * 
     * This method uses ValueDAO to efficiently retrieve all stored key-value pairs
     * and returns them as a list of ValueDTO objects containing full metadata.
     * 
     * @return List of ValueDTO objects representing all stored values
     */
    public List<ValueDTO> getAll() {
        // Create a ValueDAO to handle data operations
        ValueDAO multivalues = new ValueDAO(valueRepo);
        // Retrieve all values from the repository
        multivalues.pull();
        // Return all values
        return multivalues.getAll();
    }

    /**
     * Save a new key-value pair to the database.
     * 
     * This method creates a new entry in the database with the provided key and value.
     * The system automatically generates a timestamp for the entry.
     * 
     * @param key The unique key for the value
     * @param value The value to be stored (can be any object type)
     * @return Success message confirming the save operation
     */
    public String put(String key, Object value) {
        // Insert the value into the repository
        valueRepo.insert(new values(key, value));
        // Return a success message
        return "Saved";
    }

    /**
     * Save multiple key-value pairs in a batch operation.
     * 
     * This method clears the existing database and saves all provided values
     * in a single transaction. It's designed for bulk operations where
     * complete data replacement is needed.
     * 
     * @param inputValues List of ValueDTO objects to be saved
     * @return Success message confirming the batch save operation
     */
    public String putAll(List<ValueDTO> inputValues) {
        // Create a ValueDAO to handle data operations
        ValueDAO multivalues = new ValueDAO(valueRepo);
        // Clear existing values
        multivalues.clear();
        // Save all input values
        multivalues.saveAll(inputValues);
        // Close the ValueDAO (save changes to the repository)
        multivalues.close();
        // Return a success message
        return "All values have been saved";
    }

    /**
     * Update an existing key-value pair in the database.
     * 
     * This method modifies the value of an existing key and updates the
     * lastchange timestamp to the current time. The key must exist in
     * the database for the operation to succeed.
     * 
     * @param key The key of the value to update
     * @param value The new value to be stored
     * @return Success message confirming the update operation
     */
    public String update(String key, Object value) {
        // Retrieve and update the value from the repository
        values item = valueRepo.findByKey(key);
        item.setValue(value);
        // Update the last change of the value
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        item.setLastchange(ZonedDateTime.now().format(formatter));
        // Save the value to the repository
        valueRepo.save(item);
        // Return a success message
        return "Updated";
    }

    /**
     * Delete a specific key-value pair from the database.
     * 
     * This method removes the entry with the specified key from the database.
     * The key must exist for the operation to complete successfully.
     * 
     * @param key The key of the value to be deleted
     * @return Success message confirming the deletion
     */
    public String delete(String key) {
        // Delete the value from the repository
        valueRepo.delete(valueRepo.findByKey(key));
        // Return a success message
        return "Deleted";
    }

    /**
     * Delete all key-value pairs from the database.
     * 
     * This method removes all entries from the database. Use with caution
     * as this operation cannot be undone and will result in complete data loss.
     * 
     * @return Success message confirming the bulk deletion
     */
    public String deleteAll() {
        valueRepo.deleteAll();
        return "All values have been deleted";
    }
}