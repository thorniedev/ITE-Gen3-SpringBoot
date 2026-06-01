package co.istad.ite_spring.dto;

public record CoffeeResponse(
        Integer id,
        String name,
        Integer sugar,
        Double price,
        String description
) {
}
