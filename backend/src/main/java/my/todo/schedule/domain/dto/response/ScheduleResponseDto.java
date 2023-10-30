package my.todo.schedule.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import my.todo.schedule.domain.schedule.Schedule;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleResponseDto {

    private long id;
    private Timestamp createdDate;
    @NonNull
    private String title;
    @JsonProperty("isPublic")
    private boolean isPublic = false;

    public ScheduleResponseDto(Schedule schedule){
        this.id = schedule.getId();
        this.createdDate = schedule.getCreatedDate();
        this.title = schedule.getTitle();
        this.isPublic = schedule.isPublic();
    }

    @Builder
    public ScheduleResponseDto(long id, Timestamp createdDate, @NonNull String title, boolean isPublic) {
        this.id = id;
        this.createdDate = createdDate;
        this.title = title;
        this.isPublic = isPublic;
    }

    public static ScheduleResponseDto toResponse(Schedule schedule){
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .isPublic(schedule.isPublic())
                .title(schedule.getTitle())
                .createdDate(schedule.getCreatedDate())
                .build();
    }
}
