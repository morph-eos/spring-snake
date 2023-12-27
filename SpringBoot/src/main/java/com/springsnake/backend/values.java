package com.springsnake.backend;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class values {
    @Id
    private String id;
    
    private String key;
    private String value;

    public values(String key, String value) {
        this.key = key;
        this.value = value;
    }
}