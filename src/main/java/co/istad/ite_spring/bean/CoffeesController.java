package co.istad.ite_spring.bean;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
@RequestMapping("/api/v2/coffees")
public class CoffeesController
{
    private final ArrayList<Coffee> coffeeList;
    public CoffeesController(ArrayList<Coffee> coffeeList) {
        this.coffeeList = coffeeList;
    }

    @GetMapping("/coffee")
    public ArrayList<Coffee> getCoffees() {
        return coffeeList;
    }

}
