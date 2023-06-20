package my.todo.member.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Slf4j
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String email;

//    For OAuth
    private String provider;
    private String providerId;

    @CreationTimestamp
    private Timestamp createDate;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Builder(builderClassName = "oauthBuilder", builderMethodName = "oauthBuilder")
    public User(String username, String email, Role role, String provider, String providerId) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
