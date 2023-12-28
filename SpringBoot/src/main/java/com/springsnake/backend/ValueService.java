package com.springsnake.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springsnake.backend.utils.ValueRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ValueService {
    
    @Autowired
    private final ValueRepository valueRepo;

    public Object get(String key) {
        values value = valueRepo.findByKey(key);
         if (value == null) {
            return "The value has not been found";
        } else {
            return value.getValue();
        }
    }

    public String put(String key, Object value) {
        valueRepo.insert(new values(key, value));
        return "Saved";
    }

    public String delete(String key) {
        valueRepo.delete(valueRepo.findByKey(key));
        return "Deleted";
    }
    
}
