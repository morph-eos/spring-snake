package com.springsnake.backend.utils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.springsnake.backend.values;

import lombok.Data;

@Data
public class ValueDAO implements Dao<values> {

    private ValueRepository valueRepo;
     
    private List<values> inmemorydb = valueRepo.findAll();
     
    @Override
    public Optional<values> get(long id) {
        return Optional.ofNullable(inmemorydb.get((int) id));
    }

    @Override
    public List<values> getAll() {
        return inmemorydb;
    }
        
    @Override
    public void save(values value) {
        inmemorydb.add(value);
    }
       
    @Override
    public void update(values value, String[] params) {
        value.setKey(Objects.requireNonNull(
            params[0], "Key cannot be null"));
        value.setValue(Objects.requireNonNull(
            params[1], "Value cannot be null"));
         
        inmemorydb.add(value);
    }
        
    @Override
    public void delete(values value) {
        inmemorydb.remove(value);
    }
}