package com.carnaubanco.walletservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carnaubanco.walletservice.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>{

    //já fizemos essa transação antes?
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

    boolean existsByIdempotencyKey(String idempotencyKey);

    Page<Transaction> findBySourceWalletIdOrTargetWalletId(UUID sourceId, UUID targetId, Pageable pageable);
    /*

    @Query ("""
            SELECT t FROM Transaction t
            WHERE t.sourceWallet.id = :walletId OR t.targetWallet.id = :walletID
            ORDER BY t.createdAt DESC
            """)
    Page<Transaction> findStatementByWalletId(@Param("walletId") UUID walletId, Pageable pageable);
    */
}
