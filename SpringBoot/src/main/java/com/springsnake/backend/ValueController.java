package com.springsnake.backend;

import java.util.Map;

import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ValueController {

    private final ValueService service;

    @GetMapping("/get")
    public Object get(@RequestParam("key") String key) {
        return service.get(key);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody String body) {
        Map<String, Object> parsedBody = JsonParserFactory.getJsonParser().parseMap(body);
        String res = "This value exists already"; 
        if (service.get(parsedBody.get("key").toString()) == "The value has not been found") {
            return new ResponseEntity<String> (service.save(parsedBody.get("key").toString(), parsedBody.get("value")), HttpStatus.OK);
        }
        return new ResponseEntity<String>(res, HttpStatus.CONFLICT);
    }
	
}