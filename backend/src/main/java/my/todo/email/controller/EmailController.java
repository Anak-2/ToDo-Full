package my.todo.email.controller;

import lombok.RequiredArgsConstructor;
import my.todo.email.domain.EmailMessage;
import my.todo.email.domain.request.EmailRequestDto;
import my.todo.email.domain.response.EmailResponseDto;
import my.todo.email.sevice.EmailService;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@EnableAsync
@RequiredArgsConstructor
@RequestMapping("/mail")
public class EmailController {
    private final EmailService emailService;

//    ToDo: 현재는 gmail 인증만 지원
    @PostMapping("/auth")
    public EmailResponseDto sendAuthMail(@RequestBody EmailRequestDto emailRequestDto){
        return emailService.sendAuthMail(emailRequestDto.getTo());
    }
}
