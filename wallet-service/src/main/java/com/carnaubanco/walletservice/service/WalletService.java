package com.carnaubanco.walletservice.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carnaubanco.walletservice.domain.Transaction;
import com.carnaubanco.walletservice.domain.TransactionStatus;
import com.carnaubanco.walletservice.domain.TransactionType;
import com.carnaubanco.walletservice.domain.Wallet;
import com.carnaubanco.walletservice.dtos.CreateWalletRequest;
import com.carnaubanco.walletservice.dtos.TransactionResponse;
import com.carnaubanco.walletservice.dtos.TransferRequest;
import com.carnaubanco.walletservice.dtos.WalletResponse;
import com.carnaubanco.walletservice.exception.InvalidTransferException;
import com.carnaubanco.walletservice.exception.WalletNotFoundException;
import com.carnaubanco.walletservice.repository.TransactionRepository;
import com.carnaubanco.walletservice.repository.WalletRepository;

@Service 
@Transactional (readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional 
    public WalletResponse createWallet(CreateWalletRequest request) {
        System.out.println(request);
        if (walletRepository.existsByUserId(request.userId())) {
            throw new InvalidTransferException("Usuário já tem uma carteira cadastrada");
        }

        Wallet wallet = new Wallet(request.userId());
        Wallet savedWallet = walletRepository.save(wallet);

        return WalletResponse.fromEntity(savedWallet);
    }

    public WalletResponse findById(UUID id) {
        return walletRepository.findById(id)
        .map(WalletResponse::fromEntity)
        .orElseThrow(() -> new WalletNotFoundException("Carteira não encontrada com ID: " + id));
    }

    public WalletResponse findByUserId(UUID userId) {
        return walletRepository.findByUserId(userId)
        .map(WalletResponse::fromEntity)
        .orElseThrow(() -> new WalletNotFoundException("Carteira não encontrada com usuário de ID: " + userId));
    }

    @Transactional 
    public TransactionResponse transfer(String idempotencyKey, TransferRequest request) {
        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(idempotencyKey);
        if (existingTransaction.isPresent()) {
            return TransactionResponse.fromEntity(existingTransaction.get());
        }

        Wallet sourceWallet;
        Wallet targetWallet;

        if (request.sourceWalletId().compareTo(request.targetWalletId()) < 0) {
            sourceWallet = getWalletWithLock(request.sourceWalletId(), "Origem");
            targetWallet = getWalletWithLock(request.targetWalletId(), "Destino");
        } else {
            targetWallet = getWalletWithLock(request.targetWalletId(), "Destino");
            sourceWallet = getWalletWithLock(request.sourceWalletId(), "Origem");
        }

        sourceWallet.debit(request.amount());
        targetWallet.credit(request.amount());

        Transaction transaction = new Transaction(idempotencyKey, sourceWallet, targetWallet, request.amount(), TransactionType.TRANSFER, TransactionStatus.COMPLETED);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionResponse.fromEntity(savedTransaction);
    }

    public Page<TransactionResponse> getStatement(UUID walletId, Pageable pageable) {
        if (!walletRepository.existsById(walletId)) {
            throw new WalletNotFoundException("Carteira não encontrada com ID: " + walletId);
        }
        return transactionRepository.findBySourceWalletIdOrTargetWalletId(walletId, walletId, pageable).map(TransactionResponse::fromEntity);
    }

    private Wallet getWalletWithLock(UUID walletId, String context) {
        return walletRepository.findByIdWithLock(walletId)
        .orElseThrow(() -> new WalletNotFoundException("Carteira de " + context + "não encontrada: " + walletId));
    }
}
