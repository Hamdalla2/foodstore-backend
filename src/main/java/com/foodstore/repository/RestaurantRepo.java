package com.foodstore.repository;

import com.foodstore.model.Restaurant;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepo extends MongoRepository<Restaurant, String>{
    
}
