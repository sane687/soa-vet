package ru.alex.soavet.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class AnimalDTO {

    private Long id;
    private String name;
    private String gender;
    private String birthDate;
    private String owner;
}
