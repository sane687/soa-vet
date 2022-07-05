package ru.alex.soavet.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "failed_attempt")
    private int failedAttempt;

    @Column(name = "non_locked")
    private boolean nonLocked;

    @Column(name = "lock_time")
    private Date lockTime;

    @OneToMany(mappedBy = "user")
    private Set<Animal> animals;

}
