package com.springsnake.backend.utils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueDTO {

    // Field to store the key of the value
    @NotBlank(message = "Key cannot be blank")
    @Size(min = 1, max = 100, message = "Key must be between 1 and 100 characters")
    private String key;

    // Field to store the value (of type Object)
    @NotNull(message = "Value cannot be null")
    private Object value;

    //Field to store the last change of the value
    private String lastchange;
    
}