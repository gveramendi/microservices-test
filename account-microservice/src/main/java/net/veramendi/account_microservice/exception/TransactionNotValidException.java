package net.veramendi.account_microservice.exception;

public class TransactionNotValidException extends RuntimeException {

    public TransactionNotValidException(String message) {
        super(message);
    }
}
