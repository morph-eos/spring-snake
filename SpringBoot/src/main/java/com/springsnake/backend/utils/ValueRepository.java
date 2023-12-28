package com.springsnake.backend.utils;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.springsnake.backend.values;

// Annotation to indicate that this interface is a Spring repository
@Repository
public interface ValueRepository extends MongoRepository<values, String> {

    // Method signature to find a value by its key
    values findByKey(String key);
}