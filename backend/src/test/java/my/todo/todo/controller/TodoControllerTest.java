package my.todo.todo.controller;

import jakarta.transaction.Transactional;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
import my.todo.schedule.service.ScheduleService;
import my.todo.todo.domain.dto.request.TodoRequestDto;
import my.todo.todo.domain.todo.Todo;
import my.todo.todo.repository.CustomTodoRepository;
import my.todo.todo.repository.TodoRepository;
import my.todo.todo.service.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
class TodoControllerTest {

    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    CustomScheduleRepository customScheduleRepository;
    @Autowired
    TodoService todoService;
    @Autowired
    CustomTodoRepository customTodoRepository;
    @Autowired
    TodoRepository todoRepository;

    @Test
    void todo에_schedule을_이용해_가져오는_테스트() {
        //given
        User user = User.builder()
                .username("user1")
                .password("1234")
                .build();
        userJpaRepository.save(user);
        User findUser = userJpaRepository.getByUsername("user1");
        scheduleService.add(findUser, ScheduleRequestDto.builder()
                .title("스케쥴1")
                .isPublic(false)
                .build());
        Schedule schedule = customScheduleRepository.getScheduleByTitle("스케쥴1");
        Schedule findSchedule = customScheduleRepository.getScheduleById(schedule.getId());
        ScheduleWithTodoRequest scheduleWithTodoRequest = ScheduleWithTodoRequest.builder()
                .isPublic(findSchedule.isPublic())
                .title(findSchedule.getTitle())
                .build();
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
        //when
        List<Todo> todoList = todoRepository.findBySchedule(findSchedule);
        //then
        for(Todo t : todoList){
            System.out.println(t.getContent());
        }
        Assertions.assertEquals(3, todoList.size());
    }
}