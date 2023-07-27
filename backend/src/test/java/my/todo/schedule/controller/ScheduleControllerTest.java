package my.todo.schedule.controller;

import jakarta.transaction.Transactional;
import my.todo.member.controller.UserJwtController;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserRepository;
import my.todo.member.service.UserService;
import my.todo.schedule.domain.dto.request.ScheduleRequestDto;
import my.todo.schedule.domain.dto.response.ScheduleResponseDto;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
import my.todo.schedule.service.ScheduleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    UserJwtController userJwtController;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("스케쥴을_유저에_추가하는_테스트")
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
        userRepository.save(user);
        User findUser = userRepository.getByUsername("user1");
        scheduleService.add(findUser, ScheduleRequestDto.builder()
                .title("스케쥴1")
                .isPublic(false)
                .build());
        scheduleService.add(findUser, ScheduleRequestDto.builder()
                .title("스케쥴2")
                .isPublic(false)
                .build());
        scheduleService.add(findUser, ScheduleRequestDto.builder()
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
        userRepository.save(user);
        User findUser = userRepository.getByUsername("user1");
        scheduleService.add(findUser, ScheduleRequestDto.builder()
                .title("스케쥴1")
                .isPublic(false)
                .build());
        //when
        Schedule findSchedule = customScheduleRepository.getScheduleByTitleAndUser("스케쥴1",findUser);
        //then
        User findUser2 = findSchedule.getUser();
        System.out.println(findUser2.getUsername());
        Assertions.assertEquals(findUser, findUser2);
    }
}