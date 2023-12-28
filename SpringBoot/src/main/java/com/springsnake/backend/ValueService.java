package com.springsnake.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springsnake.backend.utils.ValueDAO;
import com.springsnake.backend.utils.ValueDTO;
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

    public List<ValueDTO> getAll() {
        ValueDAO multivalues = new ValueDAO(valueRepo, null);
        multivalues.pull();
        return multivalues.getAll();
    }

    public String put(String key, Object value) {
        valueRepo.insert(new values(key, value));
        return "Saved";
    }

    public String putAll(List<ValueDTO> inputValues) {
        ValueDAO multivalues = new ValueDAO(valueRepo, null);
        multivalues.clear();
        multivalues.saveAll(inputValues);
        multivalues.close();
        return "All values have been saved";
    }

    public String delete(String key) {
        valueRepo.delete(valueRepo.findByKey(key));
        return "Deleted";
    }
    
}
