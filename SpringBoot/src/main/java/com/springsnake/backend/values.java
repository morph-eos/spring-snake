package com.springsnake.backend;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

//Value Schema
@Data
@Document
public class values {
    @Id
    private String id;

    private String key;
    private Object value;

    public values(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}