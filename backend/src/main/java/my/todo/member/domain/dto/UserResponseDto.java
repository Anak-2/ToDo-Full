package my.todo.member.domain.dto;

import jakarta.persistence.*;
import lombok.*;
import my.todo.member.domain.user.Role;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponseDto {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String provider;
    private String providerId;
    private Date createDate;
    private Role role;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo{
        private String accessToken;
        private Date accessTokenExpirationTime;
        private String refreshToken;
        private Date refreshTokenExpirationTime;
    }

    public UserResponseDto(User user){
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        email = user.getEmail();
        provider = user.getProvider();
        providerId = user.getProviderId();
        createDate = user.getCreateDate();
        role = user.getRole();
    }

}
