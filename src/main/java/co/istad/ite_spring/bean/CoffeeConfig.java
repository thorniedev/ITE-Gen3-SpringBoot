package co.istad.ite_spring.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class CoffeeConfig
{
    @Bean
    public Coffee Latte() {
        return new Coffee("C001", "Ice Latte", 2.7, true);
    }

    @Bean
    public Coffee Cappuccino() {
        return new Coffee("C002", "Ice Cappuccino", 3.0, true);
    }

    @Bean
    public Coffee Americano() {
        return new Coffee("C003", "Americano", 2.5, false);
    }

    @Bean
    public ArrayList<Coffee> coffeeList() {

        ArrayList<Coffee> coffees = new ArrayList<>();
        coffees.add(Latte());
        coffees.add(Cappuccino());
        coffees.add(Americano());

        return coffees;
    }
}
