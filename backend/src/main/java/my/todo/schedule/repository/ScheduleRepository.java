package my.todo.schedule.repository;

import lombok.Getter;
import lombok.Setter;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByTitle(String title);
}
