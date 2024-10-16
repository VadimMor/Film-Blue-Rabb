package com.film.blue_rabb.model;

import com.film.blue_rabb.enums.RoleEnum;
import com.film.blue_rabb.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "_user")
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "creation_time")
    private Timestamp creationTime;

    @Column(name = "birthday")
    private Timestamp birthday;

    @Column(name = "code", length = 4)
    private char code;

    @Column(name = "roles")
    private RoleEnum[] roles;

    @Column(name = "status")
    private StatusEnum status;

    public User(String login, String email, String password, Timestamp creationTime, Timestamp birthday, char code, RoleEnum[] roles, StatusEnum status) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.creationTime = creationTime;
        this.birthday = birthday;
        this.code = code;
        this.roles = roles;
        this.status = status;
    }
}
