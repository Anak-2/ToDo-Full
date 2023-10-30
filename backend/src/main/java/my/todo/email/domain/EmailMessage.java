package my.todo.email.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class EmailMessage {

    private String to;
    private String subject;
    private String body;

//    ToDo: 생성자랑 차이가 없는 함수... 빌더 자주 호출할 때 좋은 방안 모색하기
//    ToDo: 객체를 필드로 받아서 builder 사용하기
//    public static EmailMessage createEmailMessage(String to, String subject, String body){
//        return EmailMessage.builder()
//                .to(to)
//                .subject(subject)
//                .body(body)
//                .build();
//    }
}
