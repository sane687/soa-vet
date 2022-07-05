package ru.alex.soavet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alex.soavet.errors.AnimalResponseStatus;
import ru.alex.soavet.model.Animal;
import ru.alex.soavet.model.AnimalDTO;
import ru.alex.soavet.repository.AnimalRepository;
import ru.alex.soavet.repository.UserRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Сервисный класс реализующий логику обработки животных в БД
 */
@Service
public class AnimalService {
    AnimalRepository animalRepository;
    UserRepository userRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository, UserRepository userRepository) {
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    /**
     * Создает экземпляр животного в БД
     * @param animalDTO
     */
    public void createAnimal(AnimalDTO animalDTO){

        Animal animal = new Animal();
        animal.setName(animalDTO.getName());
        animal.setGender(animalDTO.getGender());
        animal.setBirthDate(LocalDate.parse(animalDTO.getBirthDate()));
        animal.setUser(userRepository.findUserByUsername(animalDTO.getOwner()));

        animalRepository.save(animal);
    }

    /**
     * Удаление животного в БД
     * @param id идентификатор животного
     * @return
     */
    public boolean deleteAnimal(Long id){
        if(animalRepository.findAnimalById(id)==null){
            return false;
        }
        animalRepository.deleteAnimalById(id);
        return true;
    }

    public Animal findAnimalByName(String name){
        return animalRepository.findAnimalByName(name);
    }

    public Animal findAnimalById(Long id){
        return animalRepository.findAnimalById(id);
    }

    /**
     * Получение списка животных из БД для конкретного пользователя
     * @param id идентификатор пользователя
     * @return
     */
    public Set<Animal> getUserAnimals(Long id){
        return animalRepository.getUserAnimals(id);
    }

    /**
     * Обновляет параметры животного в БД
     * @param animalDTO
     */
    public void updateAnimal(AnimalDTO animalDTO){

        Animal animal = animalRepository.findAnimalByName(animalDTO.getName());
        animal.setName(animalDTO.getName());
        animal.setGender(animalDTO.getGender());
        animal.setBirthDate(LocalDate.parse(animalDTO.getBirthDate()));
        animal.setUser(userRepository.findUserByUsername(animalDTO.getOwner()));

        animalRepository.save(animal);
    }

    /**
     * Проверяет если животное имеет валидные поля
     * @param animalDTO объект животного
     * @return множество ошибок валидации
     */
    public Set<AnimalResponseStatus> validateAnimal(AnimalDTO animalDTO){
        Set<AnimalResponseStatus> errors = new HashSet<>();
        if(animalDTO.getName()==null || animalDTO.getGender()==null || animalDTO.getBirthDate()==null
                || animalDTO.getOwner()==null){
            errors.add(AnimalResponseStatus.INCORRECT_ANIMAL);
            return errors;
        }

        if(!animalDTO.getName().matches("[А-Яа-я]+")
                || animalDTO.getName().length() < 3){
            errors.add(AnimalResponseStatus.INVALID_NAME);
        }
        if(!animalDTO.getGender().matches("[Мм]|[Жж]")){
            errors.add(AnimalResponseStatus.INVALID_GENDER);
        }
        try{
            if(LocalDate.parse(animalDTO.getBirthDate()).isAfter(LocalDate.now())){
                errors.add(AnimalResponseStatus.INVALID_BIRTH_DATE);
            }
        } catch (Exception e){
            errors.add(AnimalResponseStatus.INVALID_BIRTH_DATE);
        }

        if(userRepository.findUserByUsername(animalDTO.getOwner())==null){
            errors.add(AnimalResponseStatus.INVALID_OWNER);
        }

        return errors;
    }
}
