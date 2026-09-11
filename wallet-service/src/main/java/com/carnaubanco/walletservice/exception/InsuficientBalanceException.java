package com.carnaubanco.walletservice.exception;

public class InsuficientBalanceException extends RuntimeException {
    public InsuficientBalanceException(String message) {
        super(message);
    }

}
