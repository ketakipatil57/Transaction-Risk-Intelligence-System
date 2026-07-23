package com.pict.Service.impl;

import com.pict.Service.GroqService;
import com.pict.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GroqServiceImpl implements GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GroqServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String generateExplanation(String prompt) {

        String url = "https://api.groq.com/openai/v1/chat/completions";
        // Build Request

        Message message = new Message();
        message.setRole("user");
        message.setContent(prompt);

        GroqRequestDTO requestDTO = new GroqRequestDTO();
        requestDTO.setModel("llama-3.3-70b-versatile");
        requestDTO.setMessages(List.of(message));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<GroqRequestDTO> entity =
                new HttpEntity<>(requestDTO, headers);


        ResponseEntity<GroqResponseDTO> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        GroqResponseDTO.class
                );

        GroqResponseDTO responseBody = response.getBody();

        if (responseBody == null || responseBody.getChoices().isEmpty()) {
            throw new RuntimeException("No response received from Groq.");
        }

        return responseBody
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}