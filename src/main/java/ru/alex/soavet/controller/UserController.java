package ru.alex.soavet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.alex.soavet.errors.AnimalResponseStatus;

import ru.alex.soavet.model.*;
import ru.alex.soavet.service.AnimalService;
import ru.alex.soavet.service.UserDetailsImpl;
import ru.alex.soavet.service.UserService;
import java.util.Set;

/**
 * Контроллер для обработки операций с
 */
@RestController
@RequestMapping("/api/vet")
public class UserController {

    UserService userService;
    AnimalService animalService;

    @Autowired
    public UserController(UserService userService, AnimalService animalService) {
        this.userService = userService;
        this.animalService = animalService;
    }

    /**
     * Получение животного
     * @param id идентификатор животного
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAnimal(@PathVariable Long id){
        if(id == null){
            return new ResponseEntity<>(AnimalResponseStatus.ANIMAL_ID_MUST_BE_A_NUMBER, HttpStatus.NOT_FOUND);
        }

        Animal animal = animalService.findAnimalById(id);
        if(animal == null){
            return  new ResponseEntity<>(AnimalResponseStatus.NO_ANIMAL_WITH_SUCH_NAME, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    /**
     * Создание животного
     * @param animalDTO объект животного
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Set<AnimalResponseStatus>> createAnimal(@RequestBody AnimalDTO animalDTO){

        Set<AnimalResponseStatus> errors = animalService.validateAnimal(animalDTO);
        if(animalService.findAnimalByName(animalDTO.getName())!= null){
            errors.add(AnimalResponseStatus.ALREADY_EXISTS);
        }
        if(!errors.isEmpty()){
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }


        animalService.createAnimal(animalDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Обновление параметров животного
     * @param animalDTO объект животного
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<Set<AnimalResponseStatus>> updateAnimal(@RequestBody AnimalDTO animalDTO){
        Set<AnimalResponseStatus> errors = animalService.validateAnimal(animalDTO);

        if(animalService.findAnimalByName(animalDTO.getName()) == null){
            errors.add(AnimalResponseStatus.NO_ANIMAL_WITH_SUCH_NAME);
        }
        if(!errors.isEmpty()){
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        animalService.updateAnimal(animalDTO);
        return null;
    }

    /**
     * Удаление животного
     * @param id идентификатор животного
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity<AnimalResponseStatus> deleteAnimal(@PathVariable Long id){
        if(!animalService.deleteAnimal(id)){
            return new ResponseEntity<>(AnimalResponseStatus.NO_ANIMAL_WITH_SUCH_ID,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(AnimalResponseStatus.SUCCESS, HttpStatus.OK);
    }


    /**
     * Получение всех животных авторизированного пользователя
     * @return
     */
    @RequestMapping(value = "/get-my-animals", method = RequestMethod.GET)
    public ResponseEntity<Set<Animal>> getUserAnimals(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long id = userDetails.getId();
        return new ResponseEntity<>(animalService.getUserAnimals(id), HttpStatus.OK);
    }
}
