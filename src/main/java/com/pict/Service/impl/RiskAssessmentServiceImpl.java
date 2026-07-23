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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final RiskAssessmentRepo riskAssessmentRepo;
    @Autowired
    private final TransactionRepo transactionRepo;
    @Autowired
    private final GroqService groqService;

    public RiskAssessmentServiceImpl(RestTemplate restTemplate, RiskAssessmentRepo riskAssessmentRepo, TransactionRepo transactionRepo, GroqService groqService){
        this.restTemplate = restTemplate;
        this.riskAssessmentRepo = riskAssessmentRepo;
        this.transactionRepo = transactionRepo;
        this.groqService = groqService;
    }

    @Override
    public RiskAssessmentResponseDTO analyseTransaction(Long transactionId){
        //1. Fetch Transaction
        //        │
        //2. Check transaction exists
        //        │
        //3. Create MLRequestDTO
        //        │
        //4. Call Flask API
        //        │
        //5. Receive MLResponseDTO
        //        │
        //6. Create RiskAssessment Entity
        //        │
        //7. Save in MySQL
        //        │
        //8. Return RiskAssessmentResponseDTO


        Transaction transaction = transactionRepo.findById(transactionId).orElseThrow(()->new RuntimeException("Transaction Not Found"));

        Optional<RiskAssessment> existing    = riskAssessmentRepo.findByTransaction(transaction);

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

        // Data that will be given to flask and then to the model for prediction
        MLRequestDTO mlRequestDTO = new MLRequestDTO();
         //Amount from Db
        mlRequestDTO.setAmount(transaction.getAmount());
        //Device type , setting using conditions
        int deviceType = 0;
        switch (transaction.getDevice().toUpperCase()){
            case "MOBILE":
                deviceType = 0;
                break;

            case "WEB":
                deviceType = 1;
                break;

            case "ATM":
                deviceType = 2;
                break;

            default: deviceType = 0;
        }
        mlRequestDTO.setDeviceType(deviceType);

        //Transaction hour retrieving from DB
        int transactionHour = transaction.getTransactionTime().getHour();
        mlRequestDTO.setTransactionHour(transactionHour);

        // transactionFrequency
        long transactionFrequency = transactionRepo.countByUserId(transaction.getUser().getId());
        mlRequestDTO.setTransactionFrequency(transactionFrequency);
//
        // Setting trusted receiver
        boolean trustedReceiver = transactionRepo.existsByUserIdAndReceiver(transaction.getUser().getId(), transaction.getReceiver());
        if(trustedReceiver){
            mlRequestDTO.setTrustedReceiver(1);
        }else{
            mlRequestDTO.setTrustedReceiver(0);
        }

        // Getting previous transaction riskScore from DB
        Optional<RiskAssessment> previousAssessment =
                riskAssessmentRepo.findTopByTransactionUserIdOrderByAssessmentTimeDesc(
                        transaction.getUser().getId());

        double previousRiskScore = previousAssessment
                .map(RiskAssessment::getRiskScore)
                .orElse(0.0);

        if (previousRiskScore > 1.0) {
            previousRiskScore /= 100.0;
        }

        mlRequestDTO.setPreviousRiskScore(previousRiskScore);

        // Simulating the values
        mlRequestDTO.setNewDevice(1);
        mlRequestDTO.setLocationChanged(1);
        mlRequestDTO.setFailedAttempts(5);
        mlRequestDTO.setTrustedReceiver(0);
        mlRequestDTO.setTransactionFrequency(15L);


        System.out.println("========== ML Request ==========");
        System.out.println(mlRequestDTO);

        MLResponseDTO responseDTO = restTemplate.postForObject("http://127.0.0.1:5000/predict", mlRequestDTO, MLResponseDTO.class);
        // (URL, request Body, response type)

        System.out.println("========== ML Response ==========");
        System.out.println(responseDTO);

        String prompt = """
You are a fraud detection assistant.

Analyze the following transaction details and explain in 3-4 simple sentences why the transaction received the given risk score and risk level.

Transaction Details:
Amount: %s
Receiver: %s
Location: %s
Device: %s
Risk Score: %.2f
Risk Level: %s

Do not greet the user.
Do not repeat the input.
Only provide a concise explanation of the risk.
""".formatted(
                transaction.getAmount(),
                transaction.getReceiver(),
                transaction.getLocation(),
                transaction.getDevice(),
                responseDTO.getRiskScore(),
                responseDTO.getRiskLevel()
        );

        String explanation = groqService.generateExplanation(prompt);

        System.out.println("Groq Response: " + explanation);

        RiskAssessment riskAssessment = new RiskAssessment();
        if(responseDTO == null){
            throw new RuntimeException("No response received from ML service");
        }
        riskAssessment.setTransaction(transaction);
        riskAssessment.setRiskScore(responseDTO.getRiskScore());
        riskAssessment.setAssessmentTime(LocalDateTime.now());
        riskAssessment.setRiskLevel(responseDTO.getRiskLevel());
        riskAssessment.setLlmExplanation(explanation);

        if (responseDTO.getRiskLevel() == RiskLevel.HIGH) {
            transaction.setStatus(Status.BLOCKED);
        } else {
            transaction.setStatus(Status.SUCCESS);
        }
        transactionRepo.save(transaction);

        RiskAssessment savedRiskAssessment = riskAssessmentRepo.save(riskAssessment);

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

