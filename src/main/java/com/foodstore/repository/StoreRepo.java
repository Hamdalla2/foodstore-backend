package com.foodstore.repository;

import com.foodstore.model.Store;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreRepo extends MongoRepository<Store, String>{
    
}
