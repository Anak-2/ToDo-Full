package my.todo.schedule.domain.schedule;

import jakarta.persistence.*;
import lombok.*;
import my.todo.member.domain.user.User;
import my.todo.todo.domain.todo.Todo;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Schedule {
    @Id
    @GeneratedValue
    private long id;
    @CreatedDate
    private Date createdDate;
    @NonNull
    private String title;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy="schedule", cascade=CascadeType.ALL)
    private List<Todo> todoList;

    @Builder.Default
    private boolean isFinished = false;
    @Builder.Default
    private boolean isPublic = false;

}
