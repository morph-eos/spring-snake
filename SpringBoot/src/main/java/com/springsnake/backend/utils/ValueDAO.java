package com.springsnake.backend.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.springsnake.backend.values;

public class ValueDAO{

    private ValueRepository valueRepo;
     
    private List<values> valuesList = new ArrayList<values>();

    public List<values> getAll() {
        return valuesList;
    }

    public void saveAll(List<ValueDTO> input){
        for (ValueDTO value : input) {
            valuesList.add(new values(value.getKey(), value.getValue()));
        }    
    }

    public void close() {
        valueRepo.saveAll(valuesList);
    }

}