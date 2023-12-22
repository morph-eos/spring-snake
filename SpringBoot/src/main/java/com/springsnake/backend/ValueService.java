package com.springsnake.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ValueService {
    
    @Autowired
    private final ValueRepository valueRepo;

    public String get(String key) {
        values value = valueRepo.findByKey(key);
         if (value == null) {
            return "Not Found";
        } else {
            return value.getValue();
        }
    }

    public String save(String key, String value) {
        valueRepo.insert(new values(key, value));
        return "Saved";
    }
}
