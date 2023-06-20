package my.todo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.config.oauth.MyOAuth2UserService;
import my.todo.config.oauth.handler.CustomAuthFailureHandler;
import my.todo.config.oauth.handler.OAuthAuthenticationSuccessHandler;
import my.todo.filter.JwtAuthorizationFilter;
import my.todo.member.domain.user.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final MyOAuth2UserService myOAuth2UserService;
    private final OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;
    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // disable the session
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyCustomDsl())
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/user/user-info")
                .hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers("/user/admin")
                .hasAnyRole(Role.ADMIN.name())
                .anyRequest().permitAll();
        http
                .oauth2Login()
                .userInfoEndpoint().userService(myOAuth2UserService)// OAuth2 로그인 받아온 것을 myOAuth 서비스로 처리
                .and()
                .successHandler(oAuthAuthenticationSuccessHandler)
                .failureHandler(customAuthFailureHandler)
//                .defaultSuccessUrl()
        ;
        return http.build();
    }

    //    커스텀 필터 추가를 여기서 처리하기
    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            log.debug("Call MyCustomDsl");
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
//            cors 오류를 해결하기 위해 Controller 에 @CrossOrigin 을 붙여주는 방법도 있지만 이 방식은 필터 추가와 다르게 인증이 필요 없는 url 만 처리해줌
                    .addFilter(corsConfig.corsFilter()) // cors 에 대해 허락하는 필터
                    .addFilter(new JwtAuthorizationFilter(authenticationManager));
        }
    }
}
