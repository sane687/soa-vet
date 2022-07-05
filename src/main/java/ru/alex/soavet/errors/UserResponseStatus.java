package ru.alex.soavet.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserResponseStatus {
    USER_ALREADY_EXISTS(1, "User already exists"),
    INCORRECT_USERNAME_OR_PASSWORD(2, "Incorrect username or password"),
    USER_CREATED(3, "User created and authenticated"),
    SUCCESS(4, "Success"),
    TOO_MANY_FAILED_ATTEMPTS(5, "You failed to log in 10 times. You must wait 10 minutes before you can try again"),
    INVALID_USERNAME(6, "Username must only contain russian letters and be 3 character long minimum"),
    INVALID_PASSWORD(7, "Password must be 3 character minimum"),
    INCORRECT_USER(8, "Incorrect user");


    private final int id;
    private final String message;

    UserResponseStatus(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
