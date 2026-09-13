package com.carnaubanco.walletservice.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.carnaubanco.walletservice.dtos.CreateWalletRequest;
import com.carnaubanco.walletservice.dtos.TransactionResponse;
import com.carnaubanco.walletservice.dtos.TransferRequest;
import com.carnaubanco.walletservice.dtos.WalletResponse;
import com.carnaubanco.walletservice.service.WalletService;

import jakarta.validation.Valid;

@CrossOrigin (origins = "http://localhost:3000")
@RestController 
@RequestMapping ("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping 
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        WalletResponse response = walletService.createWallet(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.id())
        .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<WalletResponse> findWalletById(@PathVariable UUID id) {
        WalletResponse response = walletService.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping ("/by-user/{userId}")
    public ResponseEntity<WalletResponse> findWalletByUserId(@PathVariable UUID userId) {
        WalletResponse response = walletService.findByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping ("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestHeader("Idempotency-Key") String idempotencyKey, @Valid @RequestBody TransferRequest request) {
        TransactionResponse response = walletService.transfer(idempotencyKey, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping ("/{id}/statement")
    public ResponseEntity<Page<TransactionResponse>> getStatement(@PathVariable("id") UUID id, @PageableDefault (size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(walletService.getStatement(id, pageable));
    }
}
