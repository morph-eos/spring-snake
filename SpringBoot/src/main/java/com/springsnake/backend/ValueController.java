package com.springsnake.backend;

import java.util.Map;

import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ValueController {

    private final String notfound = "The value has not been found";

    private final ValueService service;

    @GetMapping("/get")
    public ResponseEntity<Object> get(@RequestParam("key") String key) {
        if (service.get(key).toString() == notfound) {
            return new ResponseEntity<Object>(notfound, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(service.get(key), HttpStatus.OK);
    }

    @PutMapping("/put")
    public ResponseEntity<String> put(@RequestBody String body) {
        Map<String, Object> parsedBody = JsonParserFactory.getJsonParser().parseMap(body);
        String res = "This value exists already"; 
        if (service.get(parsedBody.get("key").toString()) ==  notfound) {
            return new ResponseEntity<String> (service.put(parsedBody.get("key").toString(), parsedBody.get("value")), HttpStatus.OK);
        }
        return new ResponseEntity<String>(res, HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("key") String key) {
        if (service.get(key).toString() != notfound) {
            return new ResponseEntity<String> (service.delete(key), HttpStatus.OK);
        }
        return new ResponseEntity<String>(notfound, HttpStatus.NOT_FOUND);
    }
	
}