package com.pict.Service.impl;

import com.pict.Entity.Status;
import com.pict.Entity.Transaction;
import com.pict.Entity.User;
import com.pict.Repository.TransactionRepo;
import com.pict.Repository.UserRepo;
import com.pict.Service.TransactionService;
import com.pict.dto.TransactionRequestDTO;
import com.pict.dto.TransactionResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final UserRepo userRepo;

    public TransactionServiceImpl(TransactionRepo transactionRepo, UserRepo userRepo){
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
    }

    // Method for performing transaction
    @Override
    public TransactionResponseDTO performTransaction(TransactionRequestDTO transactionRequestDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setDevice(transactionRequestDTO.getDevice());
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setLocation(transactionRequestDTO.getLocation());
        transaction.setReceiver(transactionRequestDTO.getReceiver());
        transaction.setUser(user);
        transaction.setStatus(Status.PENDING);

        Transaction savedTransaction = transactionRepo.save(transaction);

        TransactionResponseDTO transactionResponse = new TransactionResponseDTO();

        transactionResponse.setTransactionId(savedTransaction.getTransactionId());
        transactionResponse.setSender(savedTransaction.getUser().getName());
        transactionResponse.setReceiver(savedTransaction.getReceiver());
        transactionResponse.setAmount(savedTransaction.getAmount());
        transactionResponse.setLocation(savedTransaction.getLocation());
        transactionResponse.setTransactionTime(savedTransaction.getTransactionTime());
        transactionResponse.setStatus(savedTransaction.getStatus());

        return transactionResponse;

    }

    @Override
    public TransactionResponseDTO getTransactionByTransactionId(Long transactionId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        Transaction transaction = transactionRepo.findById(transactionId).orElseThrow(()->new RuntimeException("Transaction Not found"));

        if(!transaction.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Access Denied");
        }

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setTransactionId(transaction.getTransactionId());
        transactionResponseDTO.setSender(transaction.getUser().getName());
        transactionResponseDTO.setReceiver(transaction.getReceiver());
        transactionResponseDTO.setAmount(transaction.getAmount());
        transactionResponseDTO.setTransactionTime(transaction.getTransactionTime());
        transactionResponseDTO.setStatus(transaction.getStatus());
        transactionResponseDTO.setLocation(transaction.getLocation());

        return transactionResponseDTO;
    }

    @Override
    public List<TransactionResponseDTO> getTransactionHistory(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepo.findByUserId(user.getId());

        List<TransactionResponseDTO> responseList = new ArrayList<TransactionResponseDTO>();

        for(Transaction transaction : transactions){
            TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
            transactionResponseDTO.setTransactionId(transaction.getTransactionId());
            transactionResponseDTO.setSender(transaction.getUser().getName());
            transactionResponseDTO.setReceiver(transaction.getReceiver());
            transactionResponseDTO.setAmount(transaction.getAmount());
            transactionResponseDTO.setTransactionTime(transaction.getTransactionTime());
            transactionResponseDTO.setStatus(transaction.getStatus());
            transactionResponseDTO.setLocation(transaction.getLocation());

            responseList.add(transactionResponseDTO);
        }
        return responseList;
    }
}
