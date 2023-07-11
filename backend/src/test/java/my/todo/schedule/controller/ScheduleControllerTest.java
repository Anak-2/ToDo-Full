package my.todo.schedule.controller;

import jakarta.transaction.Transactional;
import my.todo.member.controller.JwtLoginController;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import my.todo.member.service.UserJpaService;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.dto.request.ScheduleWithTodoRequest;
import my.todo.schedule.domain.dto.response.ScheduleResponseDto;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
import my.todo.schedule.service.ScheduleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ScheduleControllerTest {

    @Autowired
    ScheduleController scheduleController;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    CustomScheduleRepository customScheduleRepository;
    @Autowired
    JwtLoginController jwtLoginController;
    @Autowired
    UserJpaService userJpaService;
    @Autowired
    UserJpaRepository userJpaRepository;

    @Test
    void 스케쥴을_유저에_추가하는_테스트() {
        //give
        //when

        //then
    }

    @Test
    void 유저가_가지고있는_스케쥴들을_가져오는_테스트() {
        //given
        User user = User.builder()
                .username("1234")
                .password("1234")
                .build();
        userJpaRepository.save(user);
        User findUser = userJpaRepository.getByUsername("user1");
        scheduleService.add(ScheduleRequestDto.builder()
                .userId(findUser.getId())
                .title("스케쥴1")
                .isPublic(false)
                .build());
        scheduleService.add(ScheduleRequestDto.builder()
                .userId(findUser.getId())
                .title("스케쥴2")
                .isPublic(false)
                .build());
        scheduleService.add(ScheduleRequestDto.builder()
                .userId(findUser.getId())
                .title("스케쥴3")
                .isPublic(false)
                .build());
        //when
        List<ScheduleResponseDto> scheduleResponseDtoList = customScheduleRepository.getScheduleListByUser(findUser);
        //then
        for (ScheduleResponseDto s : scheduleResponseDtoList) {
            System.out.println(s.getTitle());
        }
        Assertions.assertEquals(scheduleResponseDtoList.size(), 3);
    }

    @Test
    void 스케쥴을_조회했을때_유저도_조회되는지_테스트() {
        //given
        User user = User.builder()
                .username("1234")
                .password("1234")
                .build();
        userJpaRepository.save(user);
        User findUser = userJpaRepository.getByUsername("user1");
        scheduleService.add(ScheduleRequestDto.builder()
                .userId(findUser.getId())
                .title("스케쥴1")
                .isPublic(false)
                .build());
        //when
        Schedule findSchedule = customScheduleRepository.getScheduleByTitle("스케쥴1");
        //then
        User findUser2 = findSchedule.getUser();
        System.out.println(findUser2.getUsername());
        Assertions.assertEquals(findUser, findUser2);
    }
}