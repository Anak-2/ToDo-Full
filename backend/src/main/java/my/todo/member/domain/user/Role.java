package my.todo.member.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    //    스프링 시큐리티에서는 권한 코드에 항상 "ROLE_" 이 앞에 있어야한다 ROLE(KEY, TITLE)
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String title;
}
