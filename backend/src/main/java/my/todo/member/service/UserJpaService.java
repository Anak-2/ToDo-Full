package my.todo.member.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.filter.jwt.JwtTokenProvider;
import my.todo.global.error.NotAuthorizedException;
import my.todo.global.error.UserNotFoundException;
import my.todo.member.domain.dto.UserRequestDto;
import my.todo.member.domain.dto.UserResponseDto;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserJpaService {

    private final UserJpaRepository userJpaRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //    join
    public boolean join(User user) {
//        duplicate check
        if (!userJpaRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userJpaRepository.save(user);
            return true;
        }else{
            System.out.println("중복된 아이디 존재");
            return false;
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
            log.error("로그인 실패");
        }
        return null;
    }

//    update user info
    public void userUpdate(String updateBeforeUser, UserRequestDto.UpdateDTO updateDTO) {
        Optional<User> findUser = userJpaRepository.findByUsername(updateBeforeUser);
        if(findUser.isPresent()){
//            update by "dirty checking"
            findUser.get().setPassword(updateDTO.getPassword());
        }else {
            throw new UserNotFoundException("Can't Find Such User");
        }
    }
//    logout

//    delete

//
}
