package com.pict.Controller;
import com.pict.Service.RiskAssessmentService;
import com.pict.dto.RiskAssessmentResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/risk")
public class    RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskAssessmentController(RiskAssessmentService riskAssessmentService){
        this.riskAssessmentService = riskAssessmentService;
    }

    @PostMapping("/analyze/{transactionId}")
    public RiskAssessmentResponseDTO analyseTransaction(@PathVariable Long transactionId){
        return riskAssessmentService.analyseTransaction(transactionId);
    }
}
