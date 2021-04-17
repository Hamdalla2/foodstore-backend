package com.foodstore.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.foodstore.model.Name;
import com.foodstore.model.Store;
import com.foodstore.model.Restaurant;
import com.foodstore.repository.NameRepo;
import com.foodstore.repository.StoreRepo;
import com.foodstore.repository.RestaurantRepo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

@CrossOrigin
@RestController
public class all {
    
    @Autowired
    private RestaurantRepo restrepo;
    @Autowired
    private StoreRepo storerepo;
    @Autowired
    private NameRepo namerepo;    
    @PostMapping("/add/store")
    public String addStore(@RequestBody Store store) {
        String username = store.getUsername();
        Name name = new Name();
        name.setName(username);
        Optional<Name> current=namerepo.findOne(Example.of(name));
        if(current.isPresent()){return "exists";}
        else{
            namerepo.save(name);
            storerepo.save(store);
            return "added store";
        }
    }
    @PostMapping("/add/restaurant")
    public String addRestaurant(@RequestBody Restaurant restaurant) {
        String username = restaurant.getUsername();
        Name name = new Name();
        name.setName(username);
        Optional<Name> current=namerepo.findOne(Example.of(name));
        if(current.isPresent()){return "exists";}
        else{
            namerepo.save(name);
            restrepo.save(restaurant);
            return "added restaurant";
        }
    }
    @GetMapping("/get/names")
    public List<Name> getNames() {
        return namerepo.findAll();
    }
    @GetMapping("/get/stores")
    public List<Store> getStores() {
        return storerepo.findAll();
    }
    @GetMapping("/get/restaurants")
    public List<Restaurant> getRestaurants() {
        return restrepo.findAll();
    }
    @GetMapping("/get/store/{id}")
    public Optional<Store> getStore(@PathVariable String id) {
        return storerepo.findById(id);
    }
    @GetMapping("/get/restaurant/{id}")
    public Optional<Restaurant> getRestaurant(@PathVariable String id) {
        return restrepo.findById(id);
    }
    @DeleteMapping("/delete/store/{id}")
    public String delStore(@PathVariable String id) {
        storerepo.deleteById(id);
        return "store deleted";
    }
    @DeleteMapping("/delete/restaurant/{id}")
    public String delRestaurant(@PathVariable String id) {
        restrepo.deleteById(id);
        return "restaurant deleted";
    }
    @DeleteMapping("/delete/names")
    public String delNames() {
        namerepo.deleteAll();
        return "names deleted";
    }
    @DeleteMapping("/delete/restaurants")
    public String delRestaurant() {
        restrepo.deleteAll();
        return "restaurants deleted";
    }
    @DeleteMapping("/delete/stores")
    public String delStore() {
        storerepo.deleteAll();
        return "stores deleted";
    }
    @PostMapping("/signin/store")
    public String signStore(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Store current = new Store();
        current.setUsername(username);
        current.setPassword(password);
        Optional<Store> search=storerepo.findOne(Example.of(current));
        String value = search.map(x->x.getId()).orElse("");
        if(value!=""){return "token "+value;}
        return "not found";
    }
    @PostMapping("/signin/restaurant")
    public String signRestaurant(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Restaurant current = new Restaurant();
        current.setUsername(username);
        current.setPassword(password);
        Optional<Restaurant> search=restrepo.findOne(Example.of(current));
        String value = search.map(x->x.getId()).orElse("");
        if(value!=""){return "token "+value;}
        return "not found";
    }
    @PostMapping("/add/item")
    public String addItem(@RequestBody Map<String, String> body) {
        String id = body.get("token");
        Store store = storerepo.findById(id).orElse(new Store());
        ArrayList<String> item = new ArrayList<String>();
        item.add(body.get("image"));
        item.add(body.get("name"));
        item.add(body.get("amount"));
        item.add(body.get("price"));
        ArrayList<ArrayList<String>> added = store.getItems();
        added.add(item);
        store.setItems(added);
        storerepo.save(store);
        return "added";
    }
    @PostMapping("/buy/item")
    public String buyItem(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String id = body.get("store");
        Restaurant restaurant = restrepo.findById(token).orElse(new Restaurant());
        Store store = storerepo.findById(id).orElse(new Store());
        ArrayList<String> item = new ArrayList<String>();
        item.add(body.get("image"));
        item.add(body.get("name"));
        item.add(body.get("amount"));
        item.add(body.get("price"));
        ArrayList<ArrayList<String>> added = restaurant.getItems();
        added.add(item);
        restaurant.setItems(added);
        restrepo.save(restaurant);
        String order = restaurant.getName()+"@!?!@"+body.get("name")+"@!?!@"+body.get("amount");
        HashMap <String, ArrayList<String>> orders = store.getOrders();
        ArrayList<String> pending = orders.get("pending");
        pending.add(order);
        orders.put("pending",pending);
        store.setOrders(orders);
        storerepo.save(store);
        return "added";
    }
    @PostMapping("/order/allow")
    public String allowOrder(@RequestBody Map<String, String> body) {
        String id = body.get("id");
        String index = body.get("index");
        int i = Integer.parseInt(index);
        Store store = storerepo.findById(id).orElse(new Store());
        HashMap <String, ArrayList<String>> orders = store.getOrders();
        ArrayList<String> pending = orders.get("pending");
        ArrayList<String> allowed = orders.get("allowed");
        String order = pending.get(i);
        pending.remove(i);
        allowed.add(order);
        orders.put("pending",pending);
        orders.put("allowed",allowed);
        store.setOrders(orders);
        storerepo.save(store);
        return "ok";
    }
    @PostMapping("/order/reject")
    public String rejectOrder(@RequestBody Map<String, String> body) {
        String id = body.get("id");
        String index = body.get("index");
        int i = Integer.parseInt(index);
        Store store = storerepo.findById(id).orElse(new Store());
        HashMap <String, ArrayList<String>> orders = store.getOrders();
        ArrayList<String> pending = orders.get("pending");
        ArrayList<String> rejected = orders.get("rejected");
        String order = pending.get(i);
        pending.remove(i);
        rejected.add(order);
        orders.put("pending",pending);
        orders.put("rejected",rejected);
        store.setOrders(orders);
        storerepo.save(store);
        return "ok";
    }
}
