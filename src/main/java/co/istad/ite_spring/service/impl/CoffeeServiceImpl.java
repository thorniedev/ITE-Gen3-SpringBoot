package co.istad.ite_spring.service.impl;

import co.istad.ite_spring.domain.Coffee;
import co.istad.ite_spring.dto.CreateCoffeeRequest;
import co.istad.ite_spring.dto.CoffeeResponse;
import co.istad.ite_spring.repositoty.CoffeeRepository;
import co.istad.ite_spring.service.CoffeeService;
import org.springframework.stereotype.Service;

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
        coffee.setSugar(request.sugar());
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
                coffee.getSugar(),
                coffee.getPrice(),
                coffee.getDescription()
        );
    }

    //    @Override
//    public CoffeeResponse addCoffee(CreateCoffeeRequest request) {
//
//        Coffee coffee = new Coffee();
//
//        coffee.setId(beanCoffees.size() + 1);
//        coffee.setName(request.name());
//        coffee.setSugar(request.sugar());
//        coffee.setPrice(request.price());
//        coffee.setDescription(request.description());
//
//        beanCoffees.add(coffee);
//
//        return new CoffeeResponse(
//                coffee.getId(),
//                coffee.getName(),
//                coffee.getSugar(),
//                coffee.getPrice(),
//                coffee.getDescription()
//        );
//    }

    @Override
    public Coffee getCoffeeById(Integer id) {
        return coffeeRepository.getCoffees().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Coffee> searchCoffee(String name, Double price, Integer sugar) {
        return coffeeRepository.getCoffees().stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(c -> price == 0 || c.getPrice() > price)
                .filter(c -> c.getSugar() > sugar)
                .toList();
    }

    @Override
    public List<Coffee> getAllCoffees() {
        return coffeeRepository.getCoffees();
    }

    /*

        @Override
        public Coffee getCoffeeById(Integer id) {

            List<Coffee> coffees = coffeeRepository.beanCoffees();

            return coffees.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Coffee> searchCoffee(String name, Double price, Integer sugar) {

            List<Coffee> coffees = coffeeRepository.beanCoffees();

            return coffees.stream()
                    .filter(c ->
                            c.getName().toLowerCase().contains(name.toLowerCase())
                    )
                    .filter(c ->
                            price == 0 || c.getPrice() > price
                    )
                    .filter(c -> c.getSugar() > sugar)
                    .toList();
        }

     */
}
