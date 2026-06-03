package co.istad.ite_spring.service;

import co.istad.ite_spring.domain.Coffee;
import co.istad.ite_spring.dto.CreateCoffeeRequest;
import co.istad.ite_spring.dto.CoffeeResponse;
import co.istad.ite_spring.dto.UpdateCoffeeRequest;

import java.util.List;

public interface CoffeeService {

       /**
        1. Expected result (return type: void, object, collection, int,..)
        2. Your logic: addCoffee
        3. Parameters are used for client submission
       */
        CoffeeResponse addCoffee(CreateCoffeeRequest createCoffeeRequest);

        CoffeeResponse updateCoffeeById(Integer id, UpdateCoffeeRequest updateCoffeeRequest);

        List<CoffeeResponse> getAllCoffees();

        CoffeeResponse getCoffeeById(Integer id);

        List<Coffee> searchCoffee(String name, Double price, Integer sugar);

        void deleteCoffeeById(Integer id);
}
