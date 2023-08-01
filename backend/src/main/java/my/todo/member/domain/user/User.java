package my.todo.member.domain.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import my.todo.schedule.domain.schedule.Schedule;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Slf4j
@Builder
@AllArgsConstructor
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
    @Builder.Default
    private Role role = Role.USER;

    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Schedule> scheduleList = new ArrayList<>();

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateEmail(String email){
        this.email = email;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    public void createScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

}
