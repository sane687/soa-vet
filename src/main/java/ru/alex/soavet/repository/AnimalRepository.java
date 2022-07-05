package ru.alex.soavet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alex.soavet.model.Animal;

import java.util.Set;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Animal findAnimalByName(String name);
    Animal findAnimalById(Long id);
    void deleteAnimalById(Long id);
    @Query(value = "SELECT * FROM animals where owner_id =:id", nativeQuery = true)
    Set<Animal> getUserAnimals(Long id);

}
