package my.todo.schedule.domain.schedule;

import jakarta.persistence.*;
import lombok.*;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.dto.request.ScheduleUpdateRequestDto;
import my.todo.todo.domain.todo.Todo;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void updateTitle(String title){
        this.title = title;
    }

    public void updateIsPublic(boolean isPublic){
        this.isPublic = isPublic;
    }
}
