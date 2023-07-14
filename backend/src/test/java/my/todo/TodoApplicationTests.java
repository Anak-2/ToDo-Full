package my.todo;

import jakarta.transaction.Transactional;
import my.todo.member.domain.dto.request.UserRequestDto;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import my.todo.member.service.UserJpaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class TodoApplicationTests {

	@Autowired
	UserJpaRepository userJpaRepository;
	@Autowired
	UserJpaService userJpaService;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("유저 비밀번호 업데이트")
	void 유저_정보_업데이트(){
		//given
		User save = userJpaRepository.save(User.builder()
                .username("test1")
                .password("password1")
                .build());
		//when
		String updatePassword = "password2";
		UserRequestDto.UpdateDTO updateDTO = new UserRequestDto.UpdateDTO();
		updateDTO.setUsername(save.getUsername());
		updateDTO.setPassword(updatePassword);
		userJpaService.userUpdate(save.getUsername(),updateDTO);
		//then
		User saved = userJpaRepository.findByUsername(save.getUsername()).orElseThrow();
		assertThat(saved.getPassword()).isEqualTo(updatePassword);
	}

}
