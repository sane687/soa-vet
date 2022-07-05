package ru.alex.soavet.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.soavet.errors.UserResponseStatus;
import ru.alex.soavet.model.SignUpDTO;
import ru.alex.soavet.service.UserService;
import java.util.Set;

/**
 * Контроллер для обрабоки регистрации
 */
@RestController
public class AuthController {

    AuthenticationManagerBuilder authenticationManager;
    UserService userService;

    @Autowired
    public AuthController(AuthenticationManagerBuilder authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }


    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUpDto) {
        Set<UserResponseStatus> errors = userService.validateUser(signUpDto);
        if(!errors.isEmpty()){
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        if(!userService.createUser(signUpDto)){
            return new ResponseEntity<>(UserResponseStatus.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(signUpDto.getUsername(), signUpDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(UserResponseStatus.USER_CREATED, HttpStatus.OK);
    }
}
