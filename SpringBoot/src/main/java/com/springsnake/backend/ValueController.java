package com.springsnake.backend;

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
    public String get(@RequestParam("key") String key) {
        return service.get(key);
    }
	
    @PostMapping("/save")
    public String save(@RequestBody String key, String value) {
        return service.save(key,value);
    }
	
}