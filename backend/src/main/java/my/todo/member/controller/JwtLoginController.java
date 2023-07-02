package my.todo.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.config.auth.PrincipalDetails;
import my.todo.filter.jwt.JwtTokenProvider;
import my.todo.global.error.NotAuthorizedException;
import my.todo.member.domain.dto.UserRequestDto;
import my.todo.member.domain.dto.UserResponseDto;
import my.todo.member.domain.user.Role;
import my.todo.member.domain.user.User;
import my.todo.member.service.UserJpaService;
import my.todo.schedule.domain.schedule.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class JwtLoginController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserJpaService userJpaService;

    //    login
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto.LoginDTO loginDTO, HttpServletResponse response) throws ServletException, IOException {
        UserResponseDto.TokenInfo tokenInfo = userJpaService.login(loginDTO.getUsername(), loginDTO.getPassword());
        if (tokenInfo == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No User Found", new RuntimeException("No User Found"));
        addRefreshTokenToCookie(tokenInfo.getRefreshToken(), response);
        return new ResponseEntity<>(tokenInfo, HttpStatus.OK);
    }

    //    login with authorization header
    @GetMapping(value = "/user-info")
    public ResponseEntity<UserResponseDto> userPage(HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        Filter 를 거쳐서 Request 의 header 에 AccessToken 이 유효하지 않다면 response 의 header 에 새로 발급한 AccessToken 추가
        String accessToken = response.getHeader("Authorization");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
//        return userJpaService.userInfo(accessToken, user);
        return userJpaService.userInfo(accessToken, user);
    }

    //    join ToDo: DTO 로 Request 받기
    @PostMapping(value = "/join")
    public User join(@RequestBody User user) {
        if(userJpaService.join(user)) return user;
        else throw new NotAuthorizedException("회원가입 실패");
    }

    //    logout, delete refresh token
    @PostMapping(value="/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        deleteRefreshTokenFromCookie(request, response);
    }

    //  update user info
    @PostMapping(value="/update")
    public ResponseEntity<?> update(@RequestBody UserRequestDto.UpdateDTO updateDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        user needs to be updated
        User updateBeforeUser = principalDetails.getUser();
        userJpaService.userUpdate(updateBeforeUser.getUsername(), updateDTO);
        return ResponseEntity.ok().build();
    }

    // delete user
    @DeleteMapping(value="/delete")
    public ResponseEntity<?> delete(@RequestBody UserRequestDto.DeleteDTO deleteDTO, HttpServletRequest request, HttpServletResponse response){
        User deleteBeforeUser = getUserFromAuthentication();
        userJpaService.deleteUser(deleteBeforeUser);
        deleteRefreshTokenFromCookie(request, response);
        return ResponseEntity.ok().build();
    }

    //    add refresh token to cookie
    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) throws IOException, ServletException {
        if (refreshToken != null) {
            System.out.println("add refresh token to cookie");
            Cookie cookie = new Cookie("refreshToken", URLEncoder.encode(refreshToken, "UTF-8"));
            cookie.setMaxAge((int) JwtTokenProvider.ACCESS_TOKEN_EXPIRE_TIME);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    //    delete refresh token from cookie
    public void deleteRefreshTokenFromCookie(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String cookieName = "refreshToken";
        String rJwt = null;
        for(Cookie c : cookies){
            if(cookieName.equals(c.getName())){
                rJwt = c.getValue();
            }
        }
        Cookie cookie = new Cookie("refreshToken",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //    get principal from authentication
    public User getUserFromAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return principalDetails.toEntity();
    }
}
