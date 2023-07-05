package my.todo.schedule.repository;

import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByTitle(String title);
    Optional<Schedule> findById(Long id);
    @Query("select t from Schedule s join fetch Todo t on t.id = s.id")
    List<Todo> findTodoListBySchedule(Schedule schedule);
    List<Schedule> findByUser(User user);
}
