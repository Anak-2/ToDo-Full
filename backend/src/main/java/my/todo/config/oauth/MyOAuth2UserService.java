package my.todo.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.todo.config.auth.PrincipalDetails;
import my.todo.member.domain.OAuth2UserInfo;
import my.todo.member.domain.user.User;
import my.todo.member.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("Call loadUser | your platform is : "+userRequest.getClientRegistration().getRegistrationId());

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User){

        OAuth2UserInfo oAuth2UserInfo;

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("naver")){
            oAuth2UserInfo = new OAuth2UserInfo((Map<String, Object>)attributes.get("response"), provider);
        }else{
            oAuth2UserInfo = new OAuth2UserInfo(attributes, provider);
        }

        Optional<User> findUser = userRepository.findByProviderAndProviderId(provider, oAuth2UserInfo.getProviderId());
//          가입한 회원이 없으면 자동 가입해주기
        if(!findUser.isPresent()){
            userRepository.save(oAuth2UserInfo.toEntity());
        }
//          가입한 회원이 있으면 업데이트만
        else{
            User updateUser = findUser.get();
            updateUser.updateEmail(oAuth2UserInfo.getEmail());
            userRepository.save(updateUser);
        }

        return new PrincipalDetails(oAuth2UserInfo.toEntity(),attributes);
    }
}
