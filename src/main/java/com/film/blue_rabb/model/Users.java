package com.film.blue_rabb.model;

import com.film.blue_rabb.enums.RoleEnum;
import com.film.blue_rabb.enums.StatusUserEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class Users {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private StatusUserEnum status;

    @Column(name = "active_code")
    private String activeCode;

    public Users() {
    }

    public Users(String email, String password, RoleEnum role, StatusUserEnum status, String activeCode) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.activeCode = activeCode;
    }
}
