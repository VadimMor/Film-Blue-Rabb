package com.film.blue_rabb.model;

import com.film.blue_rabb.enums.RoleEnum;
import com.film.blue_rabb.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "_user")
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleEnum role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnum status;

    @Column(name = "active_code")
    private String activeCode;

    @Column(name = "banned_time")
    private OffsetDateTime bannedTime;

    @Column(name = "date_creation")
    private OffsetDateTime dateCreation;

    public Users(String login, String email, String password, LocalDate birthday, RoleEnum role, StatusEnum status, String activeCode, OffsetDateTime bannedTime, OffsetDateTime dateCreation) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.role = role;
        this.status = status;
        this.activeCode = activeCode;
        this.bannedTime = bannedTime;
        this.dateCreation = dateCreation;
    }
}
