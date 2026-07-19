package com.pict.Entity;

import jakarta.persistence.*;
import lombok.Getter; //Lombok
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @OneToOne
    @JoinColumn(name = "transactionId")
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel;

    @Column(nullable = false)
    private Double riskScore;

    @Column(nullable = false)
    private String llmExplanation;

    @Column(nullable = false)
    private LocalDateTime assessmentTime;

}
