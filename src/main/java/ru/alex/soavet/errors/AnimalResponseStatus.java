package ru.alex.soavet.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AnimalResponseStatus {
    ALREADY_EXISTS(1, "Animal already exists"),
    NO_SUCH_OWNER(2, "No such owner found"),
    INCORRECT_ANIMAL(3, "Incorrect animal"),
    NO_ANIMAL_WITH_SUCH_NAME(4,"No animal with such name"),
    NO_ANIMAL_WITH_SUCH_ID(5,"No animal with such id"),
    ANIMAL_ID_MUST_BE_A_NUMBER(6, "Animal id must be a number"),
    INVALID_NAME(7, "Name must only contain russian letters and be 3 character long minimum"),
    INVALID_GENDER(8, "Gender must be \"М\" or \"Ж\""),
    INVALID_BIRTH_DATE(9, "Birth date must follow the pattern \"yyyy-MM-dd\" and be in the past"),
    INVALID_OWNER(10, "No such owner found"),
    SUCCESS(11, "Success");


    private final int id;
    private final String message;

    AnimalResponseStatus(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
