package my.todo.member.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import my.todo.member.domain.user.User;

public class UserRequestDto {

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

    @Getter
    @Setter
    public static class ScheduleDTO{
        @NotEmpty
        private String title;
    }
}
