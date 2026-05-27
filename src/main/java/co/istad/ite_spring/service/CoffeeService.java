package co.istad.ite_spring.service;

import co.istad.ite_spring.domain.Coffee;
import co.istad.ite_spring.dto.CoffeeResponse;

import java.util.List;

public interface CoffeeService {
    List<Coffee> getAllCoffees();
}
