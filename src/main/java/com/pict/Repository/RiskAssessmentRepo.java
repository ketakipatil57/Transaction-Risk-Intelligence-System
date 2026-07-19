package com.pict.Repository;

import com.pict.Entity.RiskAssessment;
import com.pict.Entity.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskAssessmentRepo extends JpaRepository<RiskAssessment, Long> {

    Optional<RiskAssessment> findByTransactionTransactionId(Long transactionId);

    List<RiskAssessment> findByRiskLevel(RiskLevel riskLevel);

}
