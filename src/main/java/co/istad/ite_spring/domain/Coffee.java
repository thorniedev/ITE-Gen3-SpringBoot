package co.istad.ite_spring.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coffee {

    @NotNull private String name;
    private Integer id;
    @NotNull private Integer sugar;
    @NotNull private Double price;
    @NotNull private String description;
}
