package co.istad.ite_spring.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coffee
{
    private String code;
    private String name;
    private Double price;
    private Boolean isAvailable;
}
