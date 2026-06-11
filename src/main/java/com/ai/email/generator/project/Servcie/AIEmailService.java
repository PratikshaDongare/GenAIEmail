package com.ai.email.generator.project.Servcie;

import com.ai.email.generator.project.DTO.EmailRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AIEmailService {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;
    @Value("${gemini.url}")
    private String apiURL;

    public String generateReply(EmailRequestDTO emailRequestDTO) {

        String prompt = "Generate a " + emailRequestDTO.getTone() + " email reply for this email: "
                + emailRequestDTO.getEmailText();

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String finalUrl = apiURL + "?key=" + apiKey;
        System.out.println("Calling URL: " + finalUrl);
        System.out.println("Prompt: " + prompt);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response =
                restTemplate.exchange(
                        finalUrl,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );


        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
        Map<String, Object> candidate = candidates.get(0);

        Map<String, Object> contentMap = (Map<String, Object>) candidate.get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");

        return parts.get(0).get("text").toString();
    }
}
