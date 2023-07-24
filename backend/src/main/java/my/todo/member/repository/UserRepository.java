package my.todo.member.repository;

import my.todo.global.error.UserNotFoundException;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// User -> Schedule (one to many) 컬렉션 조회는 Schedule Repository 에 맡기자
// 왜냐! JPA 관점에서 보면 FK 가 있는 Schedule (연관관계의 주인) 에서 데이터를 조작하는게 더 자연스럽기 때문
// 하지만 RDB 에선 기본적으로 양방향 연관관계이기 때문에 User <-> Schedule 양 쪽에서 조회가 가능한게 자연스럽기 때문에 생성은 해두었다
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    //    OAuth 유저를 찾기 위한 메서드
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
//    ToDo 문제: update query 만들기
//    ToDo 해결: jpql 의 dirty check 으로 save() 하면 자동으로 update 쿼리 나감

//    User 에서 Schedule 컬렉션 조회 -> 비추천! (** 클래스 위에 설명 참조) 못 쓰게 막기 위해 private 접근 제어자 + interface 이기에 의미 없는 바디 생성
    @Query("select u from User u join fetch Schedule s where u.id = :id")
    private List<Schedule> findSchedulesById(
            @Param("id") Long id){
        return null;
    };

    default User getByUsername(String username){
        return findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    default User getById(Long id){
        return findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
