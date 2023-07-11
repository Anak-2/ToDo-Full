package my.todo;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import my.todo.member.service.UserJpaService;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
import my.todo.schedule.service.ScheduleService;
import my.todo.todo.domain.dto.request.TodoRequestDto;
import my.todo.todo.repository.CustomTodoRepository;
import my.todo.todo.service.TodoService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Date;
import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.doInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final UserJpaRepository userJpaRepository;
        private final ScheduleService scheduleService;
        private final CustomScheduleRepository customScheduleRepository;
        private final TodoService todoService;

        public void doInit1() {
            // user init
            User user = new User("user1", "pw1");
            userJpaRepository.save(user);
            User findUser = userJpaRepository.getByUsername("user1");

            // schedule init
            scheduleService.add(ScheduleRequestDto.builder()
                    .userId(findUser.getId())
                    .title("스케쥴1")
                    .isPublic(false)
                    .build());
            scheduleService.add(ScheduleRequestDto.builder()
                    .userId(findUser.getId())
                    .title("스케쥴2")
                    .isPublic(false)
                    .build());
            scheduleService.add(ScheduleRequestDto.builder()
                    .userId(findUser.getId())
                    .title("스케쥴3")
                    .isPublic(false)
                    .build());
            Schedule schedule = customScheduleRepository.getScheduleByTitle("스케쥴1");
            Schedule findSchedule = customScheduleRepository.getScheduleById(schedule.getId());

            // todo init
            todoService.addTodo(TodoRequestDto.builder()
                    .title("할일1")
                    .content("내용1")
                    .isFinished(false)
                    .scheduleId(findSchedule.getId())
                    .finishDate(new Timestamp(1))
                    .build());
            todoService.addTodo(TodoRequestDto.builder()
                    .title("할일2")
                    .content("내용2")
                    .isFinished(false)
                    .scheduleId(findSchedule.getId())
                    .finishDate(new Timestamp(1))
                    .build());
            todoService.addTodo(TodoRequestDto.builder()
                    .title("할일3")
                    .content("내용3")
                    .isFinished(false)
                    .scheduleId(findSchedule.getId())
                    .finishDate(new Timestamp(1))
                    .build());
        }
    }
}
