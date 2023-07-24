package my.todo;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserRepository;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
import my.todo.schedule.service.ScheduleService;
import my.todo.todo.domain.dto.request.TodoRequestDto;
import my.todo.todo.service.TodoService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
        private final UserRepository userRepository;
        private final ScheduleService scheduleService;
        private final CustomScheduleRepository customScheduleRepository;
        private final TodoService todoService;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        public void doInit1() {
            // user init
            User user = User.builder()
                    .username("1234")
                    .password("1234")
                    .build();
            user.updatePassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            User findUser = userRepository.getByUsername("1234");

            // schedule init
            scheduleService.add(findUser, ScheduleRequestDto.builder()
                    .title("스케쥴1")
                    .isPublic(true)
                    .build());
            scheduleService.add(findUser, ScheduleRequestDto.builder()
                    .title("스케쥴2")
                    .isPublic(false)
                    .build());
            scheduleService.add(findUser, ScheduleRequestDto.builder()
                    .title("스케쥴3")
                    .isPublic(false)
                    .build());
            Schedule schedule = customScheduleRepository.getScheduleByTitleAndUser("스케쥴1", findUser);
            Schedule findSchedule = customScheduleRepository.getScheduleById(schedule.getId());

            // todo init
            todoService.addTodo(TodoRequestDto.builder()
                    .title("할일1")
                    .content("내용1")
                    .isFinished(true)
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
