package my.todo.member.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {
    @Getter
    @Setter
    public static class LoginDTO{
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }
}
