package com.pict.Service.impl;

import com.pict.Entity.RiskAssessment;
import com.pict.Entity.RiskLevel;
import com.pict.Entity.Status;
import com.pict.Entity.Transaction;
import com.pict.Repository.RiskAssessmentRepo;
import com.pict.Repository.TransactionRepo;
import com.pict.Service.GroqService;
import com.pict.Service.RiskAssessmentService;
import com.pict.dto.MLRequestDTO;
import com.pict.dto.MLResponseDTO;
import com.pict.dto.RiskAssessmentResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final RestTemplate restTemplate;
    private final RiskAssessmentRepo riskAssessmentRepo;
    private final TransactionRepo transactionRepo;
    private final GroqService groqService;

    public RiskAssessmentServiceImpl(RestTemplate restTemplate, RiskAssessmentRepo riskAssessmentRepo, TransactionRepo transactionRepo, GroqService groqService){
        this.restTemplate = restTemplate;
        this.riskAssessmentRepo = riskAssessmentRepo;
        this.transactionRepo = transactionRepo;
        this.groqService = groqService;
    }

    @Override
    public RiskAssessmentResponseDTO analyseTransaction(Long transactionId){
        // 1. Fetch Transaction
        Transaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction Not Found"));

        // 2. Check existing assessment
        Optional<RiskAssessment> existing = riskAssessmentRepo.findByTransaction(transaction);

        if(existing.isPresent()) {
            RiskAssessment existingAssessment = existing.get();
            RiskAssessmentResponseDTO existingResponse = new RiskAssessmentResponseDTO();

            existingResponse.setTransactionId(existingAssessment.getTransaction().getTransactionId());
            existingResponse.setAssessmentId(existingAssessment.getAssessmentId());
            existingResponse.setRiskScore(existingAssessment.getRiskScore());
            existingResponse.setRiskLevel(existingAssessment.getRiskLevel());
            existingResponse.setAssessmentTime(existingAssessment.getAssessmentTime());
            existingResponse.setLlmExplanation(existingAssessment.getLlmExplanation());

            return existingResponse;
        }

        // 3. Create MLRequestDTO
        MLRequestDTO mlRequestDTO = new MLRequestDTO();
        mlRequestDTO.setAmount(transaction.getAmount());

        // Device type mapping
        int deviceType = 0;
        if (transaction.getDevice() != null) {
            switch (transaction.getDevice().toUpperCase()){
                case "MOBILE": deviceType = 0; break;
                case "WEB": deviceType = 1; break;
                case "ATM": deviceType = 2; break;
                default: deviceType = 0;
            }
        }
        mlRequestDTO.setDeviceType(deviceType);

        // Transaction hour
        int transactionHour = transaction.getTransactionTime().getHour();
        mlRequestDTO.setTransactionHour(transactionHour);

        // Transaction frequency
        long transactionFrequency = transactionRepo.countByUserId(transaction.getUser().getId());
        mlRequestDTO.setTransactionFrequency(transactionFrequency);

        // Real dynamic trusted receiver check
        boolean isTrusted = transactionRepo.existsByUserIdAndReceiver(transaction.getUser().getId(), transaction.getReceiver());
        mlRequestDTO.setTrustedReceiver(isTrusted ? 1 : 0);

        // Get previous risk score
        Optional<RiskAssessment> previousAssessment =
                riskAssessmentRepo.findTopByTransactionUserIdOrderByAssessmentTimeDesc(transaction.getUser().getId());

        double previousRiskScore = previousAssessment
                .map(RiskAssessment::getRiskScore)
                .orElse(0.0);

        if (previousRiskScore > 1.0) {
            previousRiskScore /= 100.0;
        }
        mlRequestDTO.setPreviousRiskScore(previousRiskScore);

        // Default baseline flags
        mlRequestDTO.setLocationChanged(0);
        mlRequestDTO.setFailedAttempts(0);

        // Dynamic High-Risk Trigger using compareTo() for BigDecimal
        BigDecimal highAmountThreshold = new BigDecimal("10000.0");
        if (transaction.getAmount() != null && transaction.getAmount().compareTo(highAmountThreshold) > 0) {
            mlRequestDTO.setNewDevice(1); // Flag as new device for large amounts
        } else {
            mlRequestDTO.setNewDevice(0);
        }

        System.out.println("========== ML Request ==========");
        System.out.println(mlRequestDTO);

        // 4. Call Flask ML API
        MLResponseDTO responseDTO = restTemplate.postForObject("https://transaction-risk-ml-service.onrender.com/predict", mlRequestDTO, MLResponseDTO.class);

        System.out.println("========== ML Response ==========");
        System.out.println(responseDTO);

        if (responseDTO == null) {
            throw new RuntimeException("No response received from ML service");
        }

        // 5. Build prompt for Groq with concise instruction
        String prompt = """
You are a fraud detection assistant.

Analyze the transaction details and explain in 2-3 short sentences (MAXIMUM 30 WORDS) why it received this risk level.

Transaction Details:
Amount: %s
Receiver: %s
Location: %s
Device: %s
Risk Score: %.2f
Risk Level: %s
New Device: %s
Location Changed: %s
Failed Attempts: %d

Do not greet. Be precise on the exact risk factors.
""".formatted(
                transaction.getAmount(),
                transaction.getReceiver(),
                transaction.getLocation(),
                transaction.getDevice(),
                responseDTO.getRiskScore(),
                responseDTO.getRiskLevel(),
                mlRequestDTO.getNewDevice() == 1 ? "Yes" : "No",
                mlRequestDTO.getLocationChanged() == 1 ? "Yes" : "No",
                mlRequestDTO.getFailedAttempts()
        );

        String explanation = groqService.generateExplanation(prompt);
        System.out.println("Groq Response: " + explanation);

        // 6. Create RiskAssessment Entity
        RiskAssessment riskAssessment = new RiskAssessment();
        riskAssessment.setTransaction(transaction);
        riskAssessment.setRiskScore(responseDTO.getRiskScore());
        riskAssessment.setAssessmentTime(LocalDateTime.now());
        riskAssessment.setRiskLevel(responseDTO.getRiskLevel());
        riskAssessment.setLlmExplanation(explanation);

        // 7. Update Transaction Status and Save
        if (responseDTO.getRiskLevel() == RiskLevel.HIGH) {
            transaction.setStatus(Status.BLOCKED);
        } else {
            transaction.setStatus(Status.SUCCESS);
        }
        transactionRepo.save(transaction);

        RiskAssessment savedRiskAssessment = riskAssessmentRepo.save(riskAssessment);

        // 8. Return Response DTO
        RiskAssessmentResponseDTO riskAssessmentResponseDTO = new RiskAssessmentResponseDTO();
        riskAssessmentResponseDTO.setTransactionId(savedRiskAssessment.getTransaction().getTransactionId());
        riskAssessmentResponseDTO.setAssessmentTime(savedRiskAssessment.getAssessmentTime());
        riskAssessmentResponseDTO.setRiskLevel(savedRiskAssessment.getRiskLevel());
        riskAssessmentResponseDTO.setRiskScore(savedRiskAssessment.getRiskScore());
        riskAssessmentResponseDTO.setLlmExplanation(savedRiskAssessment.getLlmExplanation());
        riskAssessmentResponseDTO.setAssessmentId(savedRiskAssessment.getAssessmentId());

        return riskAssessmentResponseDTO;
    }
}