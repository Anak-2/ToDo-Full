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

}
