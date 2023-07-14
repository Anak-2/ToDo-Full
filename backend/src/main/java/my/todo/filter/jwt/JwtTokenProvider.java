package my.todo.filter.jwt;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import my.todo.config.auth.PrincipalDetails;
import my.todo.member.domain.dto.response.UserResponseDto;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

//JWT Token 관리하는 메서드
@Slf4j
@Component
public class JwtTokenProvider {

    public static final String BEARER_TYPE = "Bearer ";
    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 20*1000L; // 20s
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 7*24*60*60*1000L; // 7d

    private static String secretKey;
    private static UserJpaRepository userJpaRepository;
    
//    User Jpa Repository 와 @Value 를 스프링 컨테이너한테서 받기
    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, UserJpaRepository userJpaRepository){
        JwtTokenProvider.secretKey = secretKey;
        JwtTokenProvider.userJpaRepository = userJpaRepository;
    }

    /*
    *   Generate Access Token and Refresh Token with using Authentication (Authenticated User Info)
    */
    public static UserResponseDto.TokenInfo generateToken(Authentication authentication){
        String username = authentication.getName();
        String accessToken = generateAccessToken(username);
        String refreshToken = generateRefreshToken();

        return UserResponseDto.TokenInfo.builder()
                .accessToken(accessToken)
                .accessTokenExpirationTime(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .build();
    }

    /*
    *  Generate Access Token
    * */
    public static String generateAccessToken(String username){
        return BEARER_TYPE + JWT.create()
                .withSubject("jwtToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .withClaim("username", username)
                .withClaim("type",TYPE_ACCESS)
                .sign(HMAC512(secretKey));
    }

    /*
    *  Generate Refresh Token
    * */
    public static String generateRefreshToken(){
        return BEARER_TYPE + JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .withClaim("type",TYPE_REFRESH)
                .sign(HMAC512(secretKey));
    }

    /*
    *   Refresh Access Token
    *   @Parameter: refresh token info and invalid access token
    *   @Return: refreshed access token
    * */
    public static String refreshAccessToken(String refreshToken, String accessToken){
        try{
            log.debug("Refresh Access Token");
//          RefreshToken 유효한지 검사
            DecodedJWT rJwt = JWT.require(HMAC512(secretKey)).build()
                    .verify(refreshToken);
//            ToDo: RefreshToken 저장하는 저장소 따로 만들기 -> refresh token 이 유효하지만 서버에 없을 경우 오류 처리 못함

//          AccessToken 에서 사용자 정보 추출
            DecodedJWT jwt = JWT.decode(accessToken);
            String username = jwt.getClaim("username").asString();
            if(userJpaRepository.existsByUsername(username)){
    //          새 AccessToken 생성
                return generateAccessToken(username);
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Such User Found");
            }
        }
        catch(TokenExpiredException e){
            log.error("Refresh Token is Expired on "+e.getExpiredOn());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Refresh Token is Expired on "+e.getExpiredOn(),e);
        }catch(SignatureVerificationException sve){
            log.error("Refresh Signature is invalidate");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Refresh Signature is invalidate",sve);
        }
    }

    /*
    *   Get Authentication from access token
    * */
    public static Authentication getAuthentication(String accessToken){
        try{
            DecodedJWT jwt = JWT.require(HMAC512(secretKey)).build()
                    .verify(accessToken);
            String username = jwt.getClaim("username").asString();
            if (username != null && !username.equals("")) {
                User user = userJpaRepository.findByUsername(username).orElse(null);
                if(user != null){
                    PrincipalDetails principalDetails = new PrincipalDetails(user);
                    return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                }
            }
        }
        catch(TokenExpiredException e){
            log.error("Access Token is Expired on "+e.getExpiredOn());
        }catch(SignatureVerificationException sve){
            log.error("Access Signature is invalidate");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Access Signature is invalidate",sve);
        }
        return null;
    }
}
