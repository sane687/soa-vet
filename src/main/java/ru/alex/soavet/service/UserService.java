package ru.alex.soavet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.soavet.errors.UserResponseStatus;
import ru.alex.soavet.model.SignUpDTO;
import ru.alex.soavet.model.User;
import ru.alex.soavet.repository.AnimalRepository;
import ru.alex.soavet.repository.UserRepository;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Сервисный класс реализующий логику обработки пользователей в БД
 */
@Service
@Transactional
public class UserService {

    public static final int MAX_FAILED_ATTEMPTS = 10;
    private static final long LOCK_TIME_DURATION = 10 *60 * 1000;

    UserRepository userRepository;
    AnimalRepository animalRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepository userRepository, AnimalRepository animalRepository) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
    }

    /**
     * Создает пользователя с указанными параметрами в БД
     * @param userDTO объект пользователя
     * @return
     */
    public boolean createUser(SignUpDTO userDTO){

        User DBUser = findUserByUsername(userDTO.getUsername());
        if(DBUser != null){
            return false;
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setNonLocked(true);

        userRepository.save(user);
        return true;
    }

    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }
    public User findUserById(Long id){
        return userRepository.findUserById(id);
    }

    /**
     * Инкрементирует счетчик неудачных попыток авторизации пользователя
     * @param user объект пользователя
     */
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.increaseFailedAttempts(newFailAttempts, user.getUsername());
    }

    /**
     * Обнуляет счетчик неудачных попыток авторизации пользователя
     * @param username имя пользователя
     */
    public void resetFailedAttempts(String username) {
        userRepository.resetFailedAttempts(username);
    }

    /**
     * Блокирует пользователя
     * @param user объект пользователя
     */
    public void lock(User user) {
        if(!userRepository.findUserByUsername(user.getUsername()).isNonLocked()){
            return;
        }
        user.setNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    /**
     * Разблокирует пользователя если время блокировки подошло к концу
     * @param user объект пользователя
     * @return
     */
    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Проверяет если пользователь имеет валидные поля
     * @param signUpDTO объект пользователя
     * @return
     */
    public Set<UserResponseStatus> validateUser(SignUpDTO signUpDTO){
        Set<UserResponseStatus> errors = new HashSet<>();

        if(signUpDTO.getUsername()==null || signUpDTO.getPassword()==null){
            errors.add(UserResponseStatus.INCORRECT_USER);
            return errors;
        }

        if (!signUpDTO.getUsername().matches("[А-Яа-я]+")
                || signUpDTO.getUsername().length() < 3) {
            errors.add(UserResponseStatus.INVALID_USERNAME);
        }

        if (signUpDTO.getPassword().length() < 3) {
            errors.add(UserResponseStatus.INVALID_PASSWORD);
        }
        return errors;
    }


}
