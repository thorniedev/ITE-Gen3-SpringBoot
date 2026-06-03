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

        Coffee coffee = new Coffee(10, "Ice late", "For me", 3.0);
        Coffee coffee1 = new Coffee(11, "Ice Cappuccino", "For me", 2.0);

        coffees.add(coffee);
        coffees.add(coffee1);

    }

    public List<Coffee> getCoffees() {
        return coffees;
    }
}
