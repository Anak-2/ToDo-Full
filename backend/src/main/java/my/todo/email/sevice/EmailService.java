package my.todo.email.sevice;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import my.todo.email.domain.EmailMessage;
import my.todo.email.domain.response.EmailResponseDto;
import my.todo.member.service.UserService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static my.todo.email.domain.EmailSubject.AUTH_SUBJECT;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private final UserService userService;

    public EmailResponseDto sendAuthMail(String to){

        String tempKey = createCode();

        String html = setContext(tempKey);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(to)
                .subject(AUTH_SUBJECT.getSubject())
                .body(html)
                .build();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getBody(), true);
            javaMailSender.send(mimeMessage);
            return new EmailResponseDto(tempKey);
        }catch(MessagingException e){
            throw new RuntimeException(e);
        }
    }

    // 4자리 난수 생성
    public String createCode() {
        return String.valueOf((int)((Math.random() * 8999) + 1000));
    }

    //타임리프를 이용한 context 설정
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code",code);
        return templateEngine.process("auth-email",context); //email.html
    }
}
