package my.todo.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import my.todo.member.domain.user.User;

import java.util.Date;

public class UserResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo{
        private String accessToken;
        private Date accessTokenExpirationTime;
        private String refreshToken;
        private Date refreshTokenExpirationTime;
    }

    //    User info + Token info ( when access token is republished, this response is required to change old one )
    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserAndTokenInfo{
        private User user;
        private String accessToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ScheduleDTO{

    }
}
