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

/**
 * REST Controller for Spring Snake Key-Value Store API
 * 
 * This controller provides a comprehensive REST API for managing key-value pairs
 * in a MongoDB database. It supports all CRUD operations with proper validation,
 * error handling, and logging.
 * 
 * Features:
 * - Single and batch save operations
 * - Flexible retrieval (value only or full object)
 * - Individual and bulk delete operations
 * - Comprehensive error handling and validation
 * - Detailed logging for monitoring and debugging
 * 
 * All endpoints return appropriate HTTP status codes and error messages
 * for proper client-side error handling.
 * 
 * @author M04ph3u2
 * @version 2.0
 * @since 1.0
 */
@RequestMapping("/api")
@RestController
@AllArgsConstructor
@Validated
@Slf4j
@CrossOrigin(origins = "*") // TODO: Configure specific origins in production
public class ValueController {

    /**
     * Standard message returned when a requested key is not found in the database
     */
    private final String notfound = "The value has not been found";

    /**
     * Service layer dependency that handles all business logic for value operations
     */
    private final ValueService service;

    /**
     * Retrieve a specific value by its key (value only, not metadata).
     * 
     * This endpoint returns only the value content without additional metadata
     * like timestamps. Use /getfull for complete object information.
     * 
     * @param key The key to search for (must not be blank)
     * @return ResponseEntity containing:
     *         - 200 OK: The value as a string
     *         - 404 NOT_FOUND: If the key doesn't exist
     *         - 400 BAD_REQUEST: If key is blank or invalid
     * 
     * @example GET /api/get?key=username
     *          Response: "john_doe"
     */
    @GetMapping("/get")
    public ResponseEntity<Object> get(@RequestParam("key") @NotBlank String key) {
        log.info("API Request - Get value for key: '{}'", key);
        
        try {
            // Validate input
            if (key.trim().isEmpty()) {
                log.warn("Get request failed - empty key provided");
                return new ResponseEntity<>("Key cannot be empty", HttpStatus.BAD_REQUEST);
            }
            
            // Attempt to retrieve the value
            Object result = service.get(key.trim());
            
            // Check if the value was found
            if (result.toString().equals(notfound)) {
                log.warn("Get request - Value not found for key: '{}'", key);
                return new ResponseEntity<>(notfound, HttpStatus.NOT_FOUND);
            }
            
            // Success case
            log.info("Get request successful - Retrieved value for key: '{}'", key);
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Get request failed - Unexpected error for key '{}': {}", key, e.getMessage(), e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieve complete object information for a specific key.
     * 
     * This endpoint returns the full ValueDTO object including key, value,
     * and metadata such as last modification timestamp.
     * 
     * @param key The key to search for
     * @return ResponseEntity containing:
     *         - 200 OK: Complete ValueDTO object with metadata
     *         - 404 NOT_FOUND: If the key doesn't exist
     *         - 400 BAD_REQUEST: If key is blank or invalid
     *         - 500 INTERNAL_SERVER_ERROR: For unexpected errors
     * 
     * @example GET /api/getfull?key=username
     *          Response: {"key":"username","value":"john_doe","lastchange":"2025-07-27T10:15:30.123Z[UTC]"}
     */
    @GetMapping("/getfull")
    public ResponseEntity<Object> getfull(@RequestParam("key") @NotBlank String key) {
        log.info("API Request - Get full object for key: '{}'", key);
        
        try {
            // Validate input
            if (key.trim().isEmpty()) {
                log.warn("GetFull request failed - empty key provided");
                return new ResponseEntity<>("Key cannot be empty", HttpStatus.BAD_REQUEST);
            }
            
            // Retrieve the full object with metadata
            ValueDTO fullResult = service.getFull(key.trim());
            
            // Check if the value was found
            if (fullResult == null) {
                log.warn("GetFull request - Value not found for key: '{}'", key);
                return new ResponseEntity<>(notfound, HttpStatus.NOT_FOUND);
            }
            
            log.info("GetFull request successful - Retrieved full object for key: '{}'", key);
            return new ResponseEntity<>(fullResult, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("GetFull request failed - Unexpected error for key '{}': {}", key, e.getMessage(), e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Retrieve all key-value pairs from the database.
     * 
     * This endpoint returns all stored key-value pairs with their metadata.
     * Use with caution in production environments with large datasets
     * as it returns all data without pagination.
     * 
     * @return ResponseEntity containing:
     *         - 200 OK: List of all ValueDTO objects (may be empty)
     *         - 500 INTERNAL_SERVER_ERROR: For unexpected errors
     * 
     * @example GET /api/getall
     *          Response: [{"key":"user1","value":"john","lastchange":"2025-07-27T10:15:30.123Z[UTC]"}]
     * 
     * @apiNote Consider implementing pagination for production use with large datasets
     */
    @GetMapping("/getall")
    public ResponseEntity<Object> getAll() {
        log.info("API Request - Get all values");
        
        try {
            List<ValueDTO> allValues = service.getAll();
            log.info("GetAll request successful - Retrieved {} values", allValues.size());
            return new ResponseEntity<>(allValues, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("GetAll request failed - Unexpected error: {}", e.getMessage(), e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add or create a new key-value pair in the database.
     * 
     * This endpoint creates a new key-value pair if the key doesn't exist.
     * If the key already exists, it returns a conflict status to maintain
     * data integrity. Use the /update endpoint to modify existing values.
     * 
     * @param value The ValueDTO object containing key and value (key and value required)
     * @return ResponseEntity containing:
     *         - 201 CREATED: If the value was successfully created
     *         - 409 CONFLICT: If the key already exists
     *         - 400 BAD_REQUEST: If input validation fails
     *         - 500 INTERNAL_SERVER_ERROR: For unexpected errors
     * 
     * @example PUT /api/put
     *          Body: {"key":"username","value":"john_doe"}
     *          Response: "Value has been inserted successfully"
     */
    @PutMapping("/put")
    public ResponseEntity<String> put(@Valid @RequestBody ValueDTO value) {
        log.info("API Request - Put value for key: '{}'", value.getKey());
        
        try {
            // Validate input - key validation is handled by @Valid annotation
            if (value.getKey() == null || value.getKey().trim().isEmpty()) {
                log.warn("Put request failed - empty key provided");
                return new ResponseEntity<>("Key cannot be empty", HttpStatus.BAD_REQUEST);
            }
            
            // Validate value exists and is not empty/null
            if (value.getValue() == null) {
                log.warn("Put request failed - null value provided for key: '{}'", value.getKey());
                return new ResponseEntity<>("Value cannot be null", HttpStatus.BAD_REQUEST);
            }
            
            // Additional validation for string values
            String valueStr = value.getValue().toString();
            if (valueStr.trim().isEmpty()) {
                log.warn("Put request failed - empty value provided for key: '{}'", value.getKey());
                return new ResponseEntity<>("Value cannot be empty", HttpStatus.BAD_REQUEST);
            }
            
            // Check if the value already exists
            Object existingValue = service.get(value.getKey().trim());
            if (!existingValue.toString().equals(notfound)) {
                log.warn("Put request failed - Value already exists for key: '{}'", value.getKey());
                return new ResponseEntity<>("This value exists already", HttpStatus.CONFLICT);
            }
            
            // Create the new value
            String result = service.put(value.getKey().trim(), value.getValue());
            log.info("Put request successful - Created value for key: '{}'", value.getKey());
            return new ResponseEntity<>(result, HttpStatus.CREATED);
            
        } catch (Exception e) {
            log.error("Put request failed - Unexpected error for key '{}': {}", value.getKey(), e.getMessage(), e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add or create multiple new key-value pairs in the database.
     * 
     * This endpoint creates multiple key-value pairs in a single transaction.
     * If any key already exists, the entire operation is rejected to maintain
     * data consistency. All keys must be unique within the request and not
     * exist in the database.
     * 
     * @param inputValues List of ValueDTO objects containing keys and values
     * @return ResponseEntity containing:
     *         - 200 OK: If all values were successfully created
     *         - 409 CONFLICT: If any key already exists
     *         - 400 BAD_REQUEST: If input validation fails
     *         - 500 INTERNAL_SERVER_ERROR: For unexpected errors
     * 
     * @example PUT /api/putall
     *          Body: [{"key":"user1","value":"john"},{"key":"user2","value":"jane"}]
     *          Response: "All values have been inserted successfully"
     */
    @PutMapping("/putall")
    public ResponseEntity<String> putAll(@RequestBody List<ValueDTO> inputValues) {
        log.info("API Request - Put all values (count: {})", inputValues != null ? inputValues.size() : 0);
        
        try {
            // Validate input list
            if (inputValues == null || inputValues.isEmpty()) {
                log.warn("PutAll request failed - empty or null values list provided");
                return new ResponseEntity<>("Values list cannot be empty", HttpStatus.BAD_REQUEST);
            }
            
            // Validate each value in the list
            for (int i = 0; i < inputValues.size(); i++) {
                ValueDTO value = inputValues.get(i);
                
                // Check for null entries
                if (value == null) {
                    log.warn("PutAll request failed - null value at index {}", i);
                    return new ResponseEntity<>("Value at index " + i + " cannot be null", HttpStatus.BAD_REQUEST);
                }
                
                // Validate key
                if (value.getKey() == null || value.getKey().trim().isEmpty()) {
                    log.warn("PutAll request failed - empty key at index {}", i);
                    return new ResponseEntity<>("Key at index " + i + " cannot be empty", HttpStatus.BAD_REQUEST);
                }
                
                // Validate value
                if (value.getValue() == null) {
                    log.warn("PutAll request failed - null value at index {} for key '{}'", i, value.getKey());
                    return new ResponseEntity<>("Value at index " + i + " cannot be null", HttpStatus.BAD_REQUEST);
                }
            }
            
            // Check if any of the values already exist
            for (ValueDTO value : inputValues) {
                Object existingValue = service.get(value.getKey().trim());
                if (!existingValue.toString().equals(notfound)) {
                    log.warn("PutAll request failed - Value already exists for key: '{}'", value.getKey());
                    return new ResponseEntity<>("One of the values exists already", HttpStatus.CONFLICT);
                }
            }
            
            // All validations passed, create all values
            String result = service.putAll(inputValues);
            log.info("PutAll request successful - Created {} values", inputValues.size());
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("PutAll request failed - Unexpected error: {}", e.getMessage(), e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing key-value pair in the database.
     * 
     * This endpoint updates the value for an existing key. The key must
     * already exist in the database. Use the /put endpoint to create new
     * key-value pairs.
     * 
     * @param value The ValueDTO object containing key and new value
     * @return ResponseEntity containing:
     *         - 200 OK: If the value was successfully updated
     *         - 404 NOT_FOUND: If the key doesn't exist
     *         - 400 BAD_REQUEST: If input validation fails
     *         - 500 INTERNAL_SERVER_ERROR: For unexpected errors
     * 
     * @example POST /api/update
     *          Body: {"key":"username","value":"jane_doe"}
     *          Response: "Value has been updated successfully"
     */
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody ValueDTO value) {
        log.info("API Request - Update value for key: '{}'", value.getKey());
        
        try {
            // Validate input
            if (value.getKey() == null || value.getKey().trim().isEmpty()) {
                log.warn("Update request failed - empty key provided");
                return new ResponseEntity<>("Key cannot be empty", HttpStatus.BAD_REQUEST);
            }
            
            if (value.getValue() == null) {
                log.warn("Update request failed - null value provided for key: '{}'", value.getKey());
                return new ResponseEntity<>("Value cannot be null", HttpStatus.BAD_REQUEST);
            }
            
            // Check if the value exists
            Object existingValue = service.get(value.getKey().trim());
            if (existingValue.toString().equals(notfound)) {
                log.warn("Update request failed - Value not found for key: '{}'", value.getKey());
                return new ResponseEntity<>(notfound, HttpStatus.NOT_FOUND);
            }
            
            // Update the value
            String result = service.update(value.getKey().trim(), value.getValue());
            log.info("Update request successful - Updated value for key: '{}'", value.getKey());
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Update request failed - Unexpected error for key '{}': {}", value.getKey(), e.getMessage(), e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a specific key-value pair from the database.
     * 
     * This endpoint removes a key-value pair from the database.
     * The key must exist for the deletion to be successful.
     * 
     * @param key The key to delete (must not be blank)
     * @return ResponseEntity containing:
     *         - 200 OK: If the value was successfully deleted
     *         - 404 NOT_FOUND: If the key doesn't exist
     *         - 400 BAD_REQUEST: If key is blank or invalid
     *         - 500 INTERNAL_SERVER_ERROR: For unexpected errors
     * 
     * @example DELETE /api/delete?key=username
     *          Response: "Value has been deleted successfully"
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("key") @NotBlank String key) {
        log.info("API Request - Delete value for key: '{}'", key);
        
        try {
            // Validate input
            if (key.trim().isEmpty()) {
                log.warn("Delete request failed - empty key provided");
                return new ResponseEntity<>("Key cannot be empty", HttpStatus.BAD_REQUEST);
            }
            
            // Check if the value exists
            Object existingValue = service.get(key.trim());
            if (existingValue.toString().equals(notfound)) {
                log.warn("Delete request failed - Value not found for key: '{}'", key);
                return new ResponseEntity<>(notfound, HttpStatus.NOT_FOUND);
            }
            
            // Delete the value
            String result = service.delete(key.trim());
            log.info("Delete request successful - Deleted value for key: '{}'", key);
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Delete request failed - Unexpected error for key '{}': {}", key, e.getMessage(), e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete all key-value pairs from the database.
     * 
     * This endpoint removes all stored key-value pairs from the database.
     * Use with extreme caution as this operation is irreversible and will
     * remove all data from the system.
     * 
     * @return ResponseEntity containing:
     *         - 200 OK: If all values were successfully deleted
     *         - 500 INTERNAL_SERVER_ERROR: For unexpected errors
     * 
     * @example DELETE /api/deleteall
     *          Response: "All values have been deleted successfully"
     * 
     * @warning This operation is irreversible and will delete ALL data
     */
    @DeleteMapping("/deleteall")
    public ResponseEntity<String> deleteAll() {
        log.warn("API Request - Delete all values (DESTRUCTIVE OPERATION)");
        
        try {
            String result = service.deleteAll();
            log.warn("DeleteAll request completed - All values deleted from database");
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("DeleteAll request failed - Unexpected error: {}", e.getMessage(), e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}