package co.istad.ite_spring.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record UpdateCoffeeRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 255)
        String name,

        @NotBlank(message = "Description is required")
        @Size(min = 3, max = 10000)
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        Double price

) {
}
