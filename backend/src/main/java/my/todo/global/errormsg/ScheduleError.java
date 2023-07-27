package my.todo.global.errormsg;

import lombok.Getter;
import my.todo.schedule.service.ScheduleService;

@Getter
public enum ScheduleError {
    TITLE_DUPLICATED("제목이 중복됩니다"), SCHEDULE_NOT_FOUND("해당하는 스케쥴을 찾지 못했습니다");
    private final String msg;

    ScheduleError(String msg){
        this.msg = msg;
    }
}
