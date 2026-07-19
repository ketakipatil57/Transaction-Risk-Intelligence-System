package com.pict.dto;

import com.pict.Entity.RiskLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RiskAssessmentResponseDTO {

    private Long assessmentId;

    private Long transactionId;

    private double riskScore;

    private RiskLevel riskLevel;

    private String llmExplanation;

    private LocalDateTime assessmentTime;
}
