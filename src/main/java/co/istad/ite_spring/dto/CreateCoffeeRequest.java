package co.istad.ite_spring.dto;

import jakarta.validation.constraints.*;

public record CreateCoffeeRequest(

    @NotBlank(message = "Coffee name is required")
    @Size(min=3, max = 255)
    String name,

    @NotNull(message = "Price is required")
    @Positive
    Double price,

    @Null String description

) {
}
