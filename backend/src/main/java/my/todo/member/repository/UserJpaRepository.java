package my.todo.member.repository;

import my.todo.member.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    //    OAuth 유저를 찾기 위한 메서드
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
//    ToDo 문제: update query 만들기
//    ToDo 해결: jpql 의 dirty check 으로 save() 하면 자동으로 update 쿼리 나감
}
