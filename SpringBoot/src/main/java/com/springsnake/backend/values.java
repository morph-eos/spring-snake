package com.springsnake.backend;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    private String lastchange;

    public values(String key, Object value) {
        this.key = key;
        this.value = value;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        this.lastchange = ZonedDateTime.now().format(formatter);
    }
}