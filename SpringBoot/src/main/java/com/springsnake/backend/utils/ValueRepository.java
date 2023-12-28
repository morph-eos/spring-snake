package com.springsnake.backend.utils;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.springsnake.backend.values;

@Repository
public interface ValueRepository extends MongoRepository<values, String> {
    values findByKey(String key);
}