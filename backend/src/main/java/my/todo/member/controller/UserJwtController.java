package my.todo.member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.config.auth.PrincipalDetails;
import my.todo.filter.jwt.JwtTokenProvider;
import my.todo.member.domain.dto.request.UserRequestDto;
import my.todo.member.domain.dto.response.UserResponseDto;
import my.todo.member.domain.user.User;
import my.todo.member.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserJwtController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    //    login
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto.LoginDTO loginDTO, HttpServletResponse response) throws ServletException, IOException {
        UserResponseDto.TokenInfo tokenInfo = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
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
        return userService.userInfo(accessToken, user);
    }

    //    join
    @PostMapping(value = "/join")
    public void join(@RequestBody UserRequestDto.JoinDTO joinDTO) {
        userService.join(joinDTO);
    }

    //    logout, delete refresh token
    @PostMapping(value="/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        deleteRefreshTokenFromCookie(request, response);
    }

    //  update user info
    @PostMapping(value="/update")
    public ResponseEntity<?> update(@RequestBody UserRequestDto.UpdateDTO updateDTO){
//        user needs to be updated
        User updateBeforeUser = getUserFromAuthentication();
        userService.userUpdate(updateBeforeUser.getUsername(), updateDTO);
        return ResponseEntity.ok().build();
    }

    // delete user
    @DeleteMapping(value="/delete")
    public ResponseEntity<?> delete(@RequestBody UserRequestDto.DeleteDTO deleteDTO, HttpServletRequest request, HttpServletResponse response){
        User deleteBeforeUser = getUserFromAuthentication();
        userService.deleteUser(deleteBeforeUser);
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
