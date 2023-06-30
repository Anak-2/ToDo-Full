package my.todo.schedule.repository;

import lombok.Getter;
import lombok.Setter;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByTitle(String title);
    Optional<Schedule> findById(Long id);
    @Query("select s from Schedule s join fetch User u on s.id = u.id")
    Optional<List<Schedule>> findByUser(User user);
}
