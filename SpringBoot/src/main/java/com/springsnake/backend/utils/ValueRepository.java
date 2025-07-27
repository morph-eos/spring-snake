package com.springsnake.backend.utils;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.springsnake.backend.values;

/**
 * MongoDB Repository interface for Spring Snake Value operations
 * 
 * This interface extends MongoRepository to provide CRUD operations for the
 * values entity. It includes Spring Data MongoDB's automatic query derivation
 * and custom query methods for specific business needs.
 * 
 * The repository provides:
 * - Standard CRUD operations via MongoRepository
 * - Custom query method for finding values by key
 * - Automatic query derivation based on method names
 * 
 * @author M04ph3u2
 * @version 2.0
 * @since 1.0
 */
@Repository
public interface ValueRepository extends MongoRepository<values, String> {

    /**
     * Find a value entity by its key field
     * 
     * This method uses Spring Data's query derivation to automatically
     * generate a MongoDB query that searches for a document with the
     * specified key value.
     * 
     * @param key The key to search for in the database
     * @return The values entity with the matching key, or null if not found
     */
    values findByKey(String key);
}