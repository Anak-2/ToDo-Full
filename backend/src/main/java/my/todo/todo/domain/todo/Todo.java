package my.todo.todo.domain.todo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import my.todo.schedule.domain.schedule.Schedule;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Todo {
    @Id @GeneratedValue
    private Long id;
    @CreationTimestamp
    private Timestamp createdDate;
    private String title;
    private String content;
    private boolean isFinished;
//    ToDo: 파일 업로드를 DB 에 직접 BLOB 타입으로 넣을지, 폴더에 경로로 저장할지 결정하기
//    @Lob @Basic(fetch=FetchType.LAZY)
//    @Column(name="contents", columnDefinition = "BLOB")
//    private byte[] fileAttachment;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    private String hyperLink;


}
