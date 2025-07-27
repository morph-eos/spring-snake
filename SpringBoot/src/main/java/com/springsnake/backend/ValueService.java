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

// Lombok annotation to generate a constructor with all fields
@AllArgsConstructor
// Spring service annotation to indicate that this class provides business logic
@Service
public class ValueService {

    // Autowired annotation for automatic dependency injection of ValueRepository
    @Autowired
    private final ValueRepository valueRepo;

    // Method to get a value by key
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

    //Method to get a full value by key
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

    // Method to get all values
    public List<ValueDTO> getAll() {
        // Create a ValueDAO to handle data operations
        ValueDAO multivalues = new ValueDAO(valueRepo);
        // Retrieve all values from the repository
        multivalues.pull();
        // Return all values
        return multivalues.getAll();
    }

    // Method to add or update a value
    public String put(String key, Object value) {
        // Insert the value into the repository
        valueRepo.insert(new values(key, value));
        // Return a success message
        return "Saved";
    }

    // Method to add or update multiple values
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

    // Method to update a value by key
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

    // Method to delete a value by key
    public String delete(String key) {
        // Delete the value from the repository
        valueRepo.delete(valueRepo.findByKey(key));
        // Return a success message
        return "Deleted";
    }

    //Method to delete all values
    public String deleteAll() {
        valueRepo.deleteAll();
        return "All values have been deleted";
    }
}