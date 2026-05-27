package co.istad.ite_spring.controller;

import co.istad.ite_spring.bean.CoffeesController;
import co.istad.ite_spring.domain.Coffee;
import co.istad.ite_spring.dto.CoffeeResponse;
import co.istad.ite_spring.repositoty.CoffeeRepository;
import co.istad.ite_spring.service.CoffeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coffees")
public class CoffeeController {

    private final CoffeeService coffeeService;
    public CoffeeController(CoffeeService coffeeService){
        this.coffeeService = coffeeService;
    }

    @GetMapping
    public List<Coffee> getCoffees() {
        return coffeeService.getAllCoffees();
    }
}
