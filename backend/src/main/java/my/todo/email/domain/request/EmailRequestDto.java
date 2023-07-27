package my.todo.email.domain.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import my.todo.member.domain.user.User;

@Getter
public class EmailRequestDto {

    @Getter
    @Setter
    public static class SendDto{
        @NotEmpty
        private String to;
    }

    @Getter
    @Setter
    public static class UsernameDto{
        @NotEmpty
        private String username;
    }
}
