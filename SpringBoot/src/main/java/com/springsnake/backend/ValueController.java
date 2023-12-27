package com.springsnake.backend;

import java.util.Map;

import org.springframework.boot.json.JsonParserFactory;
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
    public String save(@RequestBody String body) {
        Map<String, Object> parsedBody = JsonParserFactory.getJsonParser().parseMap(body);
        assert !service.get(parsedBody.get("key").toString()).equals("The value has not been found") : "This value exists already";
        return service.save(parsedBody.get("key").toString(), parsedBody.get("value"));
    }
	
}