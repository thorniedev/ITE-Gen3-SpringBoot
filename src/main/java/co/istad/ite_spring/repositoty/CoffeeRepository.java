package co.istad.ite_spring.repositoty;

import co.istad.ite_spring.domain.Coffee;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CoffeeRepository {

    private final List<Coffee> coffees;
    public CoffeeRepository(List<Coffee> coffees) {

        this.coffees = new ArrayList<>();

        Coffee coffee = new Coffee("Ice late", 1, 50, 3.0, "For me");
        Coffee coffee1 = new Coffee("Ice Cappuccino", 2, 50, 2.0, "For me");
        Coffee coffee2 = new Coffee("Americano", 3, 20, 2.5, "For me");
        Coffee coffee3 = new Coffee("Green Tea", 4, 70, 1.5, "For you");
        Coffee coffee4 = new Coffee("Matcha", 5, 80, 3.5, "It not for me");

        coffees.add(coffee);
        coffees.add(coffee1);
        coffees.add(coffee2);
        coffees.add(coffee3);
        coffees.add(coffee4);
    }

    public List<Coffee> getCoffees() {
        return coffees;
    }
}
