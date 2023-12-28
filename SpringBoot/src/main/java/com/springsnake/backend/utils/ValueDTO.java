package com.springsnake.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

// Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
@Data
@AllArgsConstructor
public class ValueDTO {

    // Field to store the key of the value
    private String key;

    // Field to store the value (of type Object)
    private Object value;

    //Field to store the last change of the value
    private String lastchange;
    
}