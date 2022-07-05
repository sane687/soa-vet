package ru.alex.soavet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alex.soavet.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
    User findUserById(Long id);

    @Query(value = "UPDATE users SET failed_attempt = :failAttempts WHERE username = :username", nativeQuery = true)
    @Modifying
    void increaseFailedAttempts(int failAttempts, String username);

    @Query(value = "UPDATE users SET failed_attempt = 0 WHERE username = :username", nativeQuery = true)
    @Modifying
    void resetFailedAttempts(String username);

}
