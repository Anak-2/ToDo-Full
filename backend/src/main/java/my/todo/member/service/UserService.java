package my.todo.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.filter.jwt.JwtTokenProvider;
import my.todo.global.error.DuplicatedException;
import my.todo.global.error.UserNotFoundException;
import my.todo.member.domain.dto.request.UserRequestDto;
import my.todo.member.domain.dto.response.UserResponseDto;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserRepository;
import my.todo.schedule.domain.dto.response.ScheduleResponseDto;
import my.todo.schedule.repository.CustomScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static my.todo.global.errormsg.UserError.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
//    ToDo: User , Schedule 사이의 데이터 전송을 어떻게 구상할 지 생각하기
//    ToDo: UserController 에서 User, Schedule Service 를 가져다 DTO 를 생성하는게 좋을지
//          UserJpaService 에서 User, Schedule Repository 를 가져다 DTO 를 생성하는게 좋을지
    private final CustomScheduleRepository customScheduleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //    join
    public void join(UserRequestDto.JoinDTO joinDTO) {
//        duplicate check
        if (!userRepository.existsByUsername(joinDTO.getUsername())) {
            joinDTO.setPassword(passwordEncoder.encode(joinDTO.getPassword()));
            joinDTO.setEmail(joinDTO.getEmail()+"@gmail.com");
            userRepository.save(joinDTO.toEntity());
        }else{
            throw new DuplicatedException(DUPLICATED.getMsg());
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
            throw new UserNotFoundException(USER_NOT_EXIST.getMsg());
        }
    }

//    update user password
    public void userUpdate(String updateBeforeUser, UserRequestDto.UpdateDTO updateDTO) {
//            update by "dirty checking"
        User user = userRepository.getByUsername(updateDTO.getUsername());
        user.updatePassword(passwordEncoder.encode(updateDTO.getPassword()));
    }
//    logout

//    delete
    public void deleteUser(User user){
        try{
            userRepository.delete(user);
        }catch (IllegalArgumentException e){
            throw new UserNotFoundException(e.getMessage());
        }
    }

    public ResponseEntity<UserResponseDto> userInfo(String accessToken, User user) {
        List<ScheduleResponseDto> scheduleResponseDtoListList = customScheduleRepository.getScheduleListByUser(user);
        UserResponseDto userResponseDto = new UserResponseDto(user, scheduleResponseDtoListList);

//        check if the token has been refreshed
        if (accessToken != null) {
            return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<UserResponseDto> findUserByUsername(String username){
        User user = userRepository.getByUsername(username);
        List<ScheduleResponseDto> scheduleResponseDtoList = customScheduleRepository.getScheduleListByUser(user);
        return new ResponseEntity<>(new UserResponseDto(user, scheduleResponseDtoList), HttpStatus.OK);
    }
}
