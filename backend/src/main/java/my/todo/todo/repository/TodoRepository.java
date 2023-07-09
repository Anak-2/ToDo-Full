package my.todo.todo.repository;

import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findBySchedule(Schedule schedule);
    Optional<Todo> findByTitle(String title);
    @Query("select t from Todo t join fetch Schedule s")
    List<Todo> findAll();
}
