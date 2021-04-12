package com.foodstore.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.foodstore.model.Store;
import com.foodstore.model.Restaurant;
import com.foodstore.repository.StoreRepo;
import com.foodstore.repository.RestaurantRepo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

@RestController
public class all {
    
    @Autowired
    private RestaurantRepo restrepo;
    @Autowired
    private StoreRepo storerepo;
    @PostMapping("/add/store")
    public String addStore(@RequestBody Store store) {
        Optional<Store> current=storerepo.findOne(Example.of(store));
        if(current.isPresent()){return "exists";}
        else{
            storerepo.save(store);
            return "added store";
        }
    }
    @PostMapping("/add/restaurant")
    public String addRestaurant(@RequestBody Restaurant restaurant) {
        Optional<Restaurant> current=restrepo.findOne(Example.of(restaurant));
        if(current.isPresent()){return "exists";}
        else{
            restrepo.save(restaurant);
            return "added restaurant";
        }
    }
    @GetMapping("/get/stores")
    public List<Store> getStores() {
        return storerepo.findAll();
    }
    @GetMapping("/get/restaurants")
    public List<Restaurant> getRestaurants() {
        return restrepo.findAll();
    }
    @GetMapping("/get/stores/{id}")
    public Optional<Store> getStore(@PathVariable int id) {
        return storerepo.findById(id);
    }
    @GetMapping("/get/restaurants/{id}")
    public Optional<Restaurant> getRestaurant(@PathVariable int id) {
        return restrepo.findById(id);
    }
    @DeleteMapping("/delete/store/{id}")
    public String delStore(@PathVariable int id) {
        storerepo.deleteById(id);
        return "store deleted";
    }
    @DeleteMapping("/delete/restaurant/{id}")
    public String delRestaurant(@PathVariable int id) {
        restrepo.deleteById(id);
        return "restaurant deleted";
    }
    @PostMapping("/signin/store")
    public Optional<Store> signStore(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Store current = new Store();
        current.setUsername(username);
        current.setPassword(password);
        Optional<Store> search=storerepo.findOne(Example.of(current));
        return search;
    }
    @PostMapping("/signin/restaurant")
    public Optional<Restaurant> signRestaurant(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Restaurant current = new Restaurant();
        current.setUsername(username);
        current.setPassword(password);
        Optional<Restaurant> search=restrepo.findOne(Example.of(current));
        return search;
    }
}
