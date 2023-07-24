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

}
