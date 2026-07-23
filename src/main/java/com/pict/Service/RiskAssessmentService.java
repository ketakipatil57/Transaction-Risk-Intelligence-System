package com.pict.Service;

import com.pict.Entity.RiskAssessment;
import com.pict.Entity.Transaction;
import com.pict.dto.RiskAssessmentResponseDTO;

import java.util.Optional;

public interface RiskAssessmentService {

    RiskAssessmentResponseDTO analyseTransaction(Long transactionId);

}
