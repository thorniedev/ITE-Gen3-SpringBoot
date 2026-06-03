package co.istad.ite_spring.service.impl;

import co.istad.ite_spring.domain.Coffee;
import co.istad.ite_spring.dto.CreateCoffeeRequest;
import co.istad.ite_spring.dto.CoffeeResponse;
import co.istad.ite_spring.dto.UpdateCoffeeRequest;
import co.istad.ite_spring.repositoty.CoffeeRepository;
import co.istad.ite_spring.service.CoffeeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;

@Service
public class CoffeeServiceImpl implements CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final List<Coffee> beanCoffees;
    public CoffeeServiceImpl(CoffeeRepository coffeeRepository, List<Coffee> beanCoffees) {
        this.coffeeRepository = coffeeRepository;
        this.beanCoffees = beanCoffees;
    }

    @Override
    public CoffeeResponse addCoffee(CreateCoffeeRequest request) {

        Coffee coffee = new Coffee();

        coffee.setId(new Random().nextInt(9999) + 1);
        coffee.setName(request.name());
        coffee.setPrice(request.price());
        coffee.setDescription(request.description());

        boolean  isExisting = coffeeRepository.getCoffees()
                        .stream()
                                .anyMatch(c-> c.getId().equals(coffee.getId()));
        if(isExisting) {
            throw new RuntimeException("coffee already exists");
        }

        coffeeRepository.getCoffees().add(coffee);

        return new CoffeeResponse(
                coffee.getId(),
                coffee.getName(),
                coffee.getDescription()
        );

    }

    @Override
    public CoffeeResponse updateCoffeeById(Integer id, UpdateCoffeeRequest updateCoffeeRequest) {

        return coffeeRepository.getCoffees()
                .stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
                        .map(oldCoffee -> {
                            oldCoffee.setName(updateCoffeeRequest.name());
                            oldCoffee.setPrice(updateCoffeeRequest.price());
                            oldCoffee.setDescription(updateCoffeeRequest.description());

                            return oldCoffee;

                        })
                .map(newCoffee-> new CoffeeResponse(
                        newCoffee.getId(),
                        newCoffee.getName(),
                        newCoffee.getDescription()
                        ))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Coffee ID= %d dosen't exists is database", id)));
    }

    @Override
    public List<Coffee> searchCoffee(String name, Double price, Integer sugar) {
        return coffeeRepository.getCoffees().stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }


    @Override
    public void deleteCoffeeById(Integer id) {
        boolean removed = coffeeRepository.getCoffees()
                .removeIf(coffee -> coffee.getId().equals(id));
        if(!removed) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "coffee not found");
        }
    }

    @Override
    public List<CoffeeResponse> getAllCoffees() {
        return coffeeRepository.getCoffees().stream()
                .map(coffee -> new CoffeeResponse(coffee.getId(), coffee.getName(), coffee.getDescription()))
                .toList();
    }

    @Override
    public CoffeeResponse getCoffeeById(Integer id) {
        return coffeeRepository.getCoffees()
                .stream()
                .filter(coffee -> coffee.getId().equals(id))
                .map(coffee -> new CoffeeResponse(coffee.getId(), coffee.getName(), coffee.getDescription()))
                .findFirst()
                .orElseThrow();
    }
}
