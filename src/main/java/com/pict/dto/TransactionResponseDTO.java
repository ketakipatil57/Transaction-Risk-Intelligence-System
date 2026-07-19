package com.pict.dto;

import com.pict.Entity.RiskLevel;
import com.pict.Entity.Status;
import com.pict.Entity.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter

public class TransactionResponseDTO {


    private Long transactionId;

    private String sender;

    private String receiver;

    private BigDecimal amount;

    private String location;

    private Status status;

    private LocalDateTime transactionTime;


}
