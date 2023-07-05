package my.todo.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.filter.jwt.JwtTokenProvider;
import my.todo.global.error.DuplicatedException;
import my.todo.global.error.UserNotFoundException;
import my.todo.member.domain.dto.UserRequestDto;
import my.todo.member.domain.dto.UserResponseDto;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import my.todo.schedule.domain.dto.ScheduleResponseDto;
import my.todo.schedule.domain.schedule.Schedule;
import my.todo.schedule.repository.CustomScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserJpaService {

    private final UserJpaRepository userJpaRepository;
//    ToDo: User , Schedule 사이의 데이터 전송을 어떻게 구상할 지 생각하기
//    ToDo: UserController 에서 User, Schedule Service 를 가져다 DTO 를 생성하는게 좋을지
//          UserJpaService 에서 User, Schedule Repository 를 가져다 DTO 를 생성하는게 좋을지
    private final CustomScheduleRepository customScheduleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //    join
    public void join(User user) {
//        duplicate check
        if (!userJpaRepository.existsByUsername(user.getUsername())) {
            user.updatePassword(passwordEncoder.encode(user.getPassword()));
            userJpaRepository.save(user);
        }else{
            throw new DuplicatedException("중복된 아이디 존재");
        }
    }

    //    login using spring security
    public UserResponseDto.TokenInfo login(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(username, password);
//            AuthenticationFilter 에서 UsernamePasswordAuthenticationToken 을 가지고 AuthenticationManager 에게 전달
//            AuthenticationManager -> AuthenticationProvider -> UserDetailsService -> UserDetails
//            인증된 객체 Authentication 반환
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            return JwtTokenProvider.generateToken(authentication);
        }catch(Exception e){
//            ToDo: ExceptionHandler 를 사용하거나 throw new CustomException() 로 처리해주기
            throw new UserNotFoundException("No User Found");
        }
    }

//    update user info
    public void userUpdate(String updateBeforeUser, UserRequestDto.UpdateDTO updateDTO) {
//            update by "dirty checking"
        User user = userJpaRepository.getByUsername(updateDTO.getUsername());
        user.updatePassword(updateDTO.getPassword());
    }
//    logout

//    delete
    public void deleteUser(User user){
        try{
            userJpaRepository.delete(user);
        }catch (IllegalArgumentException e){
            throw new UserNotFoundException(e.getMessage());
        }
    }

    public ResponseEntity<UserResponseDto> userInfo(String accessToken, User user) {
        List<ScheduleResponseDto> scheduleResponseDtoListList = customScheduleRepository.getScheduleListByUser(user);
//        lazily exception 을 해결하기 위해 user 의 scheduleList 초기화
//        user.createScheduleList(scheduleList);
        UserResponseDto userResponseDto = new UserResponseDto(user);

        if (accessToken != null) {
            return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    private List<Schedule> findAllSchedule(User user){
        return userJpaRepository.findScheduleListByUser(user).orElse(null);
    }
}
