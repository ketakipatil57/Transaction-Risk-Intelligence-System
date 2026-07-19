package com.pict.Service;

import com.pict.Entity.Transaction;
import com.pict.dto.TransactionRequestDTO;
import com.pict.dto.TransactionResponseDTO;

import java.util.List;

public interface TransactionService {

    TransactionResponseDTO performTransaction(TransactionRequestDTO transactionRequestDTO);

    TransactionResponseDTO getTransactionByTransactionId(Long transactionId);

    List<TransactionResponseDTO> getTransactionHistory();
}
