package com.pict.Controller;

import com.pict.Service.TransactionService;
import com.pict.dto.TransactionRequestDTO;
import com.pict.dto.TransactionResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public TransactionResponseDTO performTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO){
        return transactionService.performTransaction(transactionRequestDTO);
    }

    @GetMapping("/transactions/{transactionId}")
    public TransactionResponseDTO getTransactionByTransactionId(@PathVariable Long transactionId){
        return transactionService.getTransactionByTransactionId(transactionId);
    }

    @GetMapping("/transactions")
    public List<TransactionResponseDTO> getTransactionHistory(){
        return transactionService.getTransactionHistory();
    }

}
