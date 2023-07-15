package my.todo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import my.todo.filter.jwt.JwtTokenProvider;
import my.todo.global.error.UserNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String accessHeader = "Authorization";
    private static final String refreshHeader = "Authorization_refresh";
    AuthenticationManager authenticationManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String accessToken = request.getHeader(accessHeader);

        log.debug("Call JwtAuthorizationFilter | accessToken :"+accessToken);

//        accessToken 이 없을 때
        if(accessToken == null || accessToken.equals("null")){
            filterChain.doFilter(request,response);
            return;
        }

//        accessToken 이 있을 때 Authentication 실행
        Authentication authentication = checkAccessToken(accessToken);
        Cookie[] cookies = request.getCookies();
//        accessToken 에 문제가 있을 때
        if(authentication == null){
            log.debug("AccessToken Has Problem");
//            RefreshToken 을 쿠키에서 가져와서 AccessToken 재발급
            String cookieName = "refreshToken";
            if(cookies == null){
                throw new UserNotFoundException("Cookie is null");
            }
            String rJwt = null;
            for(Cookie c : cookies){
                if(cookieName.equals(c.getName())){
                    rJwt = c.getValue();
                }
            }
            if(rJwt != null){
                rJwt = URLDecoder.decode(rJwt,"UTF-8");
                log.debug("refreshToken: {}",rJwt);
                rJwt = rJwt.replace(JwtTokenProvider.BEARER_TYPE, "");
//                만료된 AccessToken 이지만 사용자 정보를 추출하기 위해 Bearer 떼고 전달
                accessToken = accessToken.replace(JwtTokenProvider.BEARER_TYPE, "");
//                RefreshToken 을 이용해 AccessToken 재발급
                accessToken = JwtTokenProvider.refreshAccessToken(rJwt, accessToken);
                log.debug("refresh AccessToken: {}",accessToken);
                response.setHeader("Authorization",accessToken);
                authentication = checkAccessToken(accessToken);
            }else{
                log.debug("Require Refresh Token");
            }
        }
        if(authentication != null){
            log.debug("Authentication 완료 : {}",authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private Authentication checkAccessToken(String accessToken){
        accessToken = accessToken.replace(JwtTokenProvider.BEARER_TYPE, "");
        return JwtTokenProvider.getAuthentication(accessToken);
    }
}
