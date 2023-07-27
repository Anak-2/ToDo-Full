package my.todo.email.domain.response;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class EmailResponseDto {

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class AuthDto{
        @NotEmpty
        private final String tempKey;
    }
    
}
