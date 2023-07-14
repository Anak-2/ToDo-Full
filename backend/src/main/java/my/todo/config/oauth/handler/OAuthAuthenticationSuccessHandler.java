package my.todo.config.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.filter.jwt.JwtTokenProvider;
import my.todo.member.domain.dto.response.UserResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;

// OAuth2 로그인에 성공하면 JWT 를 생성해서 client 에게 전송
@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = "http://localhost:5501/signup/sign.html";
        UserResponseDto.TokenInfo token = JwtTokenProvider.generateToken(authentication);

//        보내는 RESPONSE 에 COOKIE 담아서 처리 (Set-Cookie)
        Cookie cookie = new Cookie("refreshToken", URLEncoder.encode(token.getRefreshToken(), "UTF-8"));
        cookie.setMaxAge((int) JwtTokenProvider.ACCESS_TOKEN_EXPIRE_TIME);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

//        보내는 URL에 ACCESS TOKEN 정보 담아서 처리 (프론트에서 URL 파싱해서 정보 처리)
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", token.getAccessToken())
                .queryParam("refreshToken", token.getRefreshToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}