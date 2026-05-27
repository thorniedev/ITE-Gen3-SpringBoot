package co.istad.ite_spring.repositoty;

import co.istad.ite_spring.domain.Coffee;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class CoffeeRepository {

    @Bean
    public List<Coffee> beanCoffees() {
        Coffee coffee = new Coffee("Ice late", 1, 50, "For me");
        Coffee coffee1 = new Coffee("Ice Cappuccino", 2, 50, "For me");
        Coffee coffee2 = new Coffee("Americano", 3, 20, "For me");

        return Arrays.asList(coffee, coffee1, coffee2);
    }

}
