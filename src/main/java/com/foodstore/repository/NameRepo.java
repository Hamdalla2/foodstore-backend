package com.foodstore.repository;

import com.foodstore.model.Name;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NameRepo extends MongoRepository<Name, Integer>{
    
}
