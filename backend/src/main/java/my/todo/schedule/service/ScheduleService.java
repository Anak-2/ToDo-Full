package my.todo.schedule.service;

import lombok.RequiredArgsConstructor;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.ScheduleRepository;
import my.todo.todo.domain.todo.Todo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public List<Todo> getTodoList(String title){
        Optional<Schedule> schedule = scheduleRepository.findByTitle(title);
        return schedule.orElseThrow().getTodoList();
    }
}
