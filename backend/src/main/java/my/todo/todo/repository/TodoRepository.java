package my.todo.todo.repository;

import my.todo.schedule.domain.schedule.Schedule;
import my.todo.todo.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByScheduleId(Long scheduleId);

    Optional<Todo> findByTitle(String title);

    @Query("select t from Todo t join fetch Schedule s")
    List<Todo> findAll();

    @Query("select t from Todo t where t.schedule.id = :schedule_id" +
            " and t.title like %:keyword%")
    List<Todo> findByKeywordLike(@Param("schedule_id") Long schedule_id,
                                 @Param("keyword") String searchInput);
}
