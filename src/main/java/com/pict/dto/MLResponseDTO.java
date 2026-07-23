package com.pict.dto;

import com.pict.Entity.RiskLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MLResponseDTO {

    private Double riskScore;

    private RiskLevel riskLevel;
}
