package com.springsnake.backend.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.springsnake.backend.values;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValueDAO{

    @Autowired
    private final ValueRepository valueRepo;
     
    private List<values> valuesList = new ArrayList<values>();

    public void pull() {
        valuesList = valueRepo.findAll();
    }

    public List<ValueDTO> getAll() {
        return valuesList.stream().map(value -> {
            ValueDTO valueDTO = new ValueDTO();
            valueDTO.setKey(value.getKey());
            valueDTO.setValue(value.getValue());
            return valueDTO;
        }).collect(Collectors.toList());
    }

    public void clear() {
        valuesList.clear();
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