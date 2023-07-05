package my.todo.member.domain.dto;

import lombok.*;
import my.todo.member.domain.user.Role;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.dto.ScheduleResponseDto;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<ScheduleResponseDto> scheduleResponseDtoList;

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
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.provider = user.getProvider();
        this.providerId = user.getProviderId();
        this.createDate = user.getCreateDate();
        this.role = user.getRole();
        this.scheduleResponseDtoList = user.getScheduleList().stream()
                        .map(o->new ScheduleResponseDto(o))
                .collect(Collectors.toList());
    }

}
