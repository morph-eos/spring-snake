package com.springsnake.backend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueRepository extends MongoRepository<values, String> {
    values findByKey(String key);
}