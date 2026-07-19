package com.pict.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequestDTO {

    private BigDecimal amount;

    private String receiver;

    private String location;

    private String device;

}
