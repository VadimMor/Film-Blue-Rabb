package com.film.blue_rabb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "ban")
@NoArgsConstructor
public class Ban {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "ban_start")
    private LocalDateTime banStart;

    @Column(name = "ban_end")
    private OffsetDateTime banEnd;

    @Column(name = "status_ban")
    private String statusBan;

    public Ban(String ip, LocalDateTime banStart, OffsetDateTime banEnd, String statusBan) {
        this.ip = ip;
        this.banStart = banStart;
        this.banEnd = banEnd;
        this.statusBan = statusBan;
    }
}
