package net.veramendi.account_microservice.controller;

import lombok.AllArgsConstructor;
import net.veramendi.account_microservice.controller.dto.CreateTransactionRequest;
import net.veramendi.account_microservice.controller.dto.TransactionResponse;
import net.veramendi.account_microservice.domain.Transaction;
import net.veramendi.account_microservice.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    private final ModelMapper modelMapper;

    @PostMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponse> createTransaction(@PathVariable String accountNumber,
                                                                 @RequestBody CreateTransactionRequest createTransactionRequest) {
        Transaction transaction = this.modelMapper.map(createTransactionRequest, Transaction.class);
        TransactionResponse createdTransaction = this.modelMapper.map(
                this.transactionService.create(accountNumber, transaction), TransactionResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @GetMapping("/accountNumber/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<TransactionResponse> transactions = new ArrayList<>();
        for (Transaction transaction : this.transactionService.getTransactionsByAccountNumber(accountNumber)) {
            transactions.add(this.modelMapper.map(transaction, TransactionResponse.class));
        }

        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }
}
