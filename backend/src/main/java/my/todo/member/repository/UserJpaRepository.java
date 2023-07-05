package my.todo.member.repository;

import my.todo.global.error.UserNotFoundException;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    //    OAuth 유저를 찾기 위한 메서드
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
//    ToDo 문제: update query 만들기
//    ToDo 해결: jpql 의 dirty check 으로 save() 하면 자동으로 update 쿼리 나감
    @Query("select s from User u join fetch Schedule s on u.id = s.user.id")
    Optional<List<Schedule>> findScheduleListByUser(User user);

    default User getByUsername(String username){
        return findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    default User getById(Long id){
        return findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
