package my.todo.email.sevice;

import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.email.domain.EmailMessage;
import my.todo.email.domain.response.EmailResponseDto;
import my.todo.filter.jwt.JwtTokenProvider;
import my.todo.global.error.duplicatedException.EmailDuplicatedException;
import my.todo.global.error.notfoundException.EmailNotFoundException;
import my.todo.global.error.notfoundException.UserNotFoundException;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserRepository;
import my.todo.member.service.UserService;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static my.todo.email.domain.EmailSubject.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private final UserRepository userRepository;

    private final UserService userService;

    public EmailResponseDto.AuthDto sendAuthMail(String to){

//        When Send Authorization Mail, occur exception if mail already exists
        if(userRepository.existsByEmail(to)) throw new EmailDuplicatedException();

        String tempKey = createCode();
        Map<String, String> map = new HashMap<>();
        map.put("code",tempKey);

        String html = setContext(map, "auth-email");

        EmailMessage emailMessage = EmailMessage.builder()
                .to(to)
                .subject(AUTH_SUBJECT.getSubject())
                .body(html)
                .build();


//        ToDo: 주소를 찾을 수 없는 이메일 (No Such User Exception) 일때 오류가 Catch 되지 않는 문제!
        sendEmail(emailMessage);
        return new EmailResponseDto.AuthDto(tempKey);
    }

    public void sendUsernameMail(String to) {
        //        When Could Not Find Mail, occur exception if mail already exists
        User findUser = userRepository.findByEmail(to).orElseThrow(EmailNotFoundException::new);

        Map<String, String> map = new HashMap<>();
        map.put("username",findUser.getUsername());

        String html = setContext(map, "username-email");

        EmailMessage emailMessage = EmailMessage.builder()
                .to(to)
                .subject(USERNAME_SUBJECT.getSubject())
                .body(html)
                .build();

        sendEmail(emailMessage);
    }

    public void sendPasswordChangeMail(String username){
        User findUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        String link = "http://localhost:5501/changePassword/changePassword.html";

        String targetUrl = UriComponentsBuilder.fromUriString(link)
                .queryParam("username", username)
                .queryParam("tempToken", JwtTokenProvider.generateAccessToken(username))
                .build().toUriString();

        Map<String, String> map = new HashMap<>();
        map.put("username", findUser.getUsername());
        map.put("changePassword", targetUrl);

        String html = setContext(map, "password-email");

        EmailMessage emailMessage = EmailMessage.builder()
                .to(findUser.getEmail())
                .subject(PASSWORD_SUBJECT.getSubject())
                .body(html)
                .build();

        sendEmail(emailMessage);
    }

    // 4자리 난수 생성
    public String createCode() {
        return String.valueOf((int)((Math.random() * 8999) + 1000));
    }

    // set variable to template
    public String setContext(Map<String, String> map, String template) {
        Context context = new Context();
        for(Map.Entry<String, String> entry : map.entrySet()){
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return templateEngine.process(template,context);
    }

    public void sendEmail(EmailMessage emailMessage){
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getBody(), true);
            javaMailSender.send(mimeMessage);
            log.info("send success");
        }catch(MessagingException e){
            log.info("send fail");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String encodeParameter(String parameter){
        try{
            return URLEncoder.encode(parameter, "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return parameter;
        }
    }
}
