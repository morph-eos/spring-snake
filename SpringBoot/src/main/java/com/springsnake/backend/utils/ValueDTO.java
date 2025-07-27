package com.springsnake.backend.utils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Spring Snake Key-Value pairs
 * 
 * This class represents a key-value pair with validation constraints and metadata
 * for data transfer between different layers of the application. It includes
 * validation annotations to ensure data integrity and consistency.
 * 
 * The DTO includes:
 * - Key: Unique identifier with size constraints
 * - Value: The stored data (can be any object type)
 * - LastChange: Timestamp of the last modification
 * 
 * @author M04ph3u2
 * @version 2.0
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueDTO {

    /**
     * Unique key identifier for the value
     * 
     * The key must be non-blank and between 1-100 characters in length.
     * This serves as the primary identifier for retrieving and manipulating values.
     */
    @NotBlank(message = "Key cannot be blank")
    @Size(min = 1, max = 100, message = "Key must be between 1 and 100 characters")
    private String key;

    /**
     * The stored value data
     * 
     * This field can contain any object type and cannot be null.
     * The value is serialized appropriately for storage and transmission.
     */
    @NotNull(message = "Value cannot be null")
    private Object value;

    /**
     * Timestamp of the last modification
     * 
     * This field stores the ISO formatted timestamp of when the value
     * was last created or updated. It's automatically managed by the system.
     */
    private String lastchange;
    
}