package com.springsnake.backend.utils;

import lombok.Data;

// Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
@Data
public class ValueDTO {

    // Field to store the key of the value
    private String key;

    // Field to store the value (of type Object)
    private Object value;
}