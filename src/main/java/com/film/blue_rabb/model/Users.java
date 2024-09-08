package com.film.blue_rabb.model;

import com.film.blue_rabb.enums.RoleEnum;
import com.film.blue_rabb.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Users(String login, String email, String password, RoleEnum role, StatusEnum status, String activeCode, OffsetDateTime bannedTime) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.activeCode = activeCode;
        this.bannedTime = bannedTime;
    }
}
