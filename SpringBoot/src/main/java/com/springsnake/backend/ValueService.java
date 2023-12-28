package com.springsnake.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.springsnake.backend.Dao;

@AllArgsConstructor
@Service
public class ValueService {
    
    @Repository
    private interface ValueRepository extends MongoRepository<values, String> {
        values findByKey(String key);
    }

    @Autowired
    private final ValueRepository valueRepo;

    @Data
    public class ValueDAO implements Dao<values> {
        
        private List<values> inmemorydb = valueRepo.findAll();
        
        @Override
        public Optional<values> get(long id) {
            return Optional.ofNullable(inmemorydb.get((int) id));
        }
    }
    private static class ValueDAO {


        private String key;
        private Object value;
    }
    
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
