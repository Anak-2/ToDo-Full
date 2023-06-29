package my.todo.member.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import my.todo.schedule.domain.schedule.Schedule;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

    @CreatedDate
    private Date createDate;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
    private List<Schedule> scheduleList = new ArrayList<>();

    @Builder(builderClassName = "normalBuilder", builderMethodName = "normalBuilder")
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
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
