package co.istad.ite_spring.bean;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
@RequestMapping("/config")
public class CoffeeController
{

    // Inject
    private final ArrayList<Coffee> coffeeList;
    public CoffeeController(ArrayList<Coffee> coffeeList) {
        this.coffeeList = coffeeList;
    }

    @GetMapping("/coffees")
    public ArrayList<Coffee> getCoffees() {
        return coffeeList;
    }
}
