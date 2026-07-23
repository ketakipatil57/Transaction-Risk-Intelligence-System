package com.pict.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class MLRequestDTO {

    private BigDecimal amount;
    private Integer deviceType;
    private Integer newDevice;
    private Integer locationChanged;
    private Integer transactionHour;
    private Long transactionFrequency;
    private Double previousRiskScore;
    private Integer trustedReceiver;
    private Integer failedAttempts;
}
