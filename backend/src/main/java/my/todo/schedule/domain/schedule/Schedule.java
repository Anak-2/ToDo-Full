package my.todo.schedule.domain.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import my.todo.member.domain.user.User;
import my.todo.todo.domain.todo.Todo;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue
    private long id;
    @CreationTimestamp
    private Timestamp createdDate;
    @NonNull
    private String title;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy="schedule", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todoList;

    @Builder.Default
    private boolean isPublic = false;

}
