package my.todo.todo.domain.todo;

import jakarta.persistence.*;
import lombok.Getter;
import my.todo.schedule.domain.schedule.Schedule;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;

@Entity
@Getter
public class Todo {
    @Id @GeneratedValue
    private Long id;
    @CreatedDate
    private Date createdDate;
    private String title;
    @Lob
    private Lob contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}
