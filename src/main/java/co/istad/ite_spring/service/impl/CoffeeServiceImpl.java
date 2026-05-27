package co.istad.ite_spring.service.impl;

import co.istad.ite_spring.domain.Coffee;
import co.istad.ite_spring.dto.CoffeeResponse;
import co.istad.ite_spring.repositoty.CoffeeRepository;
import co.istad.ite_spring.service.CoffeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoffeeServiceImpl implements CoffeeService {

    private final CoffeeRepository coffeeRepository;
    public CoffeeServiceImpl(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @Override
    public List<Coffee> getAllCoffees() {

        List<Coffee> coffees = coffeeRepository.beanCoffees();
        return coffees.stream()
                //.filter(c -> c.getId() != null)
                .toList();
    }
}
