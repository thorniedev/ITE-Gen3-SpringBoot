package co.istad.ite_spring.dto;

public record CoffeeRequest(
    String name,
    Integer sugar,
    Double price
) {
}
