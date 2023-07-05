package my.todo.schedule.repository;

import lombok.RequiredArgsConstructor;
import my.todo.global.error.ScheduleNotFoundException;
import my.todo.member.domain.user.User;
import my.todo.schedule.domain.dto.response.ScheduleResponseDto;
import my.todo.schedule.domain.schedule.Schedule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

// JpaRepository 는 Optional 로 반환하기 때문에 Service 에서 처리할 일이 많아진다
// 그러므로 get... method 로 바꿔주는 Repository 추가 생성
@Repository
@RequiredArgsConstructor
public class CustomScheduleRepository{

//    JpaRepository 를 구현한 interface 는 @EnableAutoConfiguration 에서 Spring Proxy 객체를 통해 interface 를 구현한 Bean 을 자동으로 생성해준다
//    그리고 그 이름은 interface 에 Impl 을 붙인 이름인가보다 (ScheduleRepositoryImpl 클래스로 Repository 빈을 생성하려 했지만 Circular 에러에 걸렸는데, 그것이
//    ScheduleRepository interface 에서 또 ScheduleRepositoryImpl 을 생성하려 했기 때문인가보다
    private final ScheduleRepository scheduleRepository;

    public Schedule getScheduleByTitle(String title){
        return scheduleRepository.findByTitle(title).orElseThrow(ScheduleNotFoundException::new);
    }
    public Schedule getScheduleById(Long id){
        return scheduleRepository.findById(id).orElseThrow(ScheduleNotFoundException::new);
    }

    public  List<ScheduleResponseDto> getScheduleListByUser(User user){
        return scheduleRepository.findByUser(user).stream()
                .map(schedule -> new ScheduleResponseDto(schedule))
                .collect(Collectors.toList());
    }

    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }
}
