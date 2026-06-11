package com.ai.email.generator.project.Controller;

import com.ai.email.generator.project.DTO.EmailRequestDTO;
import com.ai.email.generator.project.DTO.EmailResponseDTO;
import com.ai.email.generator.project.Servcie.AIEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("api/email")
@RequiredArgsConstructor

public class EmailController {

    private final AIEmailService aiEmailService;

    @PostMapping("/generate")
    public String generateEmail(@RequestBody EmailRequestDTO emailRequestDTO){
        try{
            return aiEmailService.generateReply(emailRequestDTO);
        }catch(HttpClientErrorException httpClientErrorException){
            return "AI service temporarily unavailable: " + httpClientErrorException.getStatusCode();
        }
    }
}
