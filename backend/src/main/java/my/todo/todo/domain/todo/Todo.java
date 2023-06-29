package my.todo.todo.domain.todo;

import jakarta.persistence.*;
import lombok.Data;
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
//    ToDo: 파일 업로드를 DB 에 직접 BLOB 타입으로 넣을지, 폴더에 경로로 저장할지 결정하기
//    @Lob @Basic(fetch=FetchType.LAZY)
//    @Column(name="contents", columnDefinition = "BLOB")
//    private byte[] contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    private String hyperLink;
}
