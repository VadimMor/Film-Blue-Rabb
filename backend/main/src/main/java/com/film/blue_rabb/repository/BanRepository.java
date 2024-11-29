package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
    /**
     * Поиск активного бана по ip
     * @param ip адрес клиента
     * @return бан
     */
    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 FROM ban " +
            "WHERE ip = :ip " +
            "AND status_ban = 'BANNED' " +
            "AND ban_end > :nowDateTime" +
            ")", nativeQuery = true)
    boolean findFirstByBannedIp(String ip, OffsetDateTime nowDateTime);
}
