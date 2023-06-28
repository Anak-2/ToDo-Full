package my.todo.schedule.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class ScheduleRequest {
    @Getter
    @Setter
    public static class ScheduleDTO{
        @NotEmpty
        String title;
    }
}
