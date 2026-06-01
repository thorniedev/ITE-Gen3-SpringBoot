package co.istad.ite_spring.controller;

import co.istad.ite_spring.domain.Coffee;
import co.istad.ite_spring.dto.CoffeeResponse;
import co.istad.ite_spring.dto.CreateCoffeeRequest;
import co.istad.ite_spring.service.CoffeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/coffees")
@RestControllerAdvice
public class CoffeeController {

    private final CoffeeService coffeeService;
    public CoffeeController(CoffeeService coffeeService){
        this.coffeeService = coffeeService;
    }

    @GetMapping
    public List<Coffee> getCoffees() {
        return coffeeService.getAllCoffees();
    }

    @GetMapping("/{id}")
    public Coffee getCoffee(
            @PathVariable Integer id
    ) {
        log.info("Get coffee by id {}", id);

        List<Coffee> coffees = coffeeService.getAllCoffees();
        return coffees.stream()
                .filter(c -> c.getId()==id)
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/search")
    public List<Coffee> seachCoffee(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") Double price,
            @RequestParam(required = false, defaultValue = "0")  Integer sugar
    ) {
        log.info("Get coffee by name {}", name);
        log.info("Get coffee by price {}", price);
        log.info("Get coffee by sugar {}", sugar);

        return coffeeService.searchCoffee(name, price, sugar);
    }

    @PostMapping
    public CoffeeResponse addCoffee(
            @Valid @RequestBody CreateCoffeeRequest createCoffeeRequest) {
        return coffeeService.addCoffee(createCoffeeRequest);
    }

}
