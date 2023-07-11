package my.todo.todo.domain.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import my.todo.schedule.domain.schedule.Schedule;
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
    private String title;
//    *** Date 객체가 아닌 Timestamp 로 바꿔서 HH.mm 표시 되도록 함 ***
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp finishDate;
    private String content;
    private boolean isFinished;
//    ToDo: 파일 업로드를 DB 에 직접 BLOB 타입으로 넣을지, 폴더에 경로로 저장할지 결정하기
//    @Lob @Basic(fetch=FetchType.LAZY)
//    @Column(name="contents", columnDefinition = "BLOB")
//    private byte[] fileAttachment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    private String hyperLink;


    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void updateFinishDate(Timestamp finishDate){
        this.finishDate = finishDate;
    }
}
