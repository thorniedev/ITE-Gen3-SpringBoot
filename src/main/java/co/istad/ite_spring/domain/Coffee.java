package co.istad.ite_spring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coffee {
    private String name;
    private Integer id;
    private Integer sugar;
    private String description;
}
