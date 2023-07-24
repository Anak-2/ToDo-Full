package my.todo.member.domain.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import my.todo.member.domain.user.User;

public class UserRequestDto {

    @Getter
    @Setter
    public static class JoinDTO{
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String email;

        public User toEntity(){
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .provider("google")
                    .build();
        }
    }

    @Getter
    @Setter
    public static class LoginDTO{
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @Getter
    @Setter
    public static class UpdateDTO{
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @Getter
    @Setter
    public static class DeleteDTO{
        @NotEmpty
        private String username;
    }
}
