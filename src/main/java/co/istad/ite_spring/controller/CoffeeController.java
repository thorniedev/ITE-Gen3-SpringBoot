package co.istad.ite_spring.controller;

import co.istad.ite_spring.domain.Coffee;
import co.istad.ite_spring.dto.CoffeeResponse;
import co.istad.ite_spring.dto.CreateCoffeeRequest;
import co.istad.ite_spring.dto.UpdateCoffeeRequest;
import co.istad.ite_spring.service.CoffeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public List<CoffeeResponse> getCoffees() {
        return coffeeService.getAllCoffees();
    }

    @GetMapping("/{id}")
    public CoffeeResponse getCoffee(
            @PathVariable Integer id
    ) {
        log.info("Get coffee by id {}", id);

        return coffeeService.getCoffeeById(id);

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

    @PutMapping("/{id}")
    public CoffeeResponse updateCoffeeById(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCoffeeRequest updateCoffeeRequest) {
        return coffeeService.updateCoffeeById(id, updateCoffeeRequest);
    }

    @PostMapping
    public CoffeeResponse addCoffee(
            @Valid @RequestBody CreateCoffeeRequest createCoffeeRequest
    ) {
        return coffeeService.addCoffee(createCoffeeRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCoffeeById(
            @PathVariable Integer id
    ) {
       coffeeService.deleteCoffeeById(id);
    }


}
