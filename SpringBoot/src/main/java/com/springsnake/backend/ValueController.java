package com.springsnake.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springsnake.backend.utils.ValueDTO;

import lombok.AllArgsConstructor;

@RequestMapping("/api")
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
    public ResponseEntity<String> put(@RequestBody ValueDTO value) {
        if (service.get(value.getKey()) ==  notfound) {
            return new ResponseEntity<String> (service.put(value.getKey(), value.getValue()), HttpStatus.OK);
        }
        return new ResponseEntity<String>("This value exists already", HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("key") String key) {
        if (service.get(key).toString() != notfound) {
            return new ResponseEntity<String> (service.delete(key), HttpStatus.OK);
        }
        return new ResponseEntity<String>(notfound, HttpStatus.NOT_FOUND);
    }
	
}