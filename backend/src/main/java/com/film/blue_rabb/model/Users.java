package com.film.blue_rabb.model;

import com.film.blue_rabb.enums.RoleEnum;
import com.film.blue_rabb.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "favorite")
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Content> contentSet;

    @CreatedDate
    @Column(name = "create_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private Date updatedDate;

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

    public void addContent(Content content) {
        if (contentSet == null) contentSet = new HashSet<>();
        if (content != null) this.contentSet.add(content);
    }
}
