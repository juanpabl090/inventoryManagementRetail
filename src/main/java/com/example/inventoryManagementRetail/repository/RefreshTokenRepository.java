package com.example.inventoryManagementRetail.repository;

import com.example.inventoryManagementRetail.model.RefreshToken;
import com.example.inventoryManagementRetail.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<com.example.inventoryManagementRetail.model.RefreshToken> findByToken(String Token);

    @Modifying
    @Transactional
    @Query("delete from RefreshToken r where r.user = :user")
    void deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("delete from RefreshToken r where r.expiryDate < :cutoffTime")
    Optional<Integer> deleteExpiredTokensOlderThan(@Param("cutoffTime") Instant cutoffTime);
}
