package my.todo.member.domain;

import my.todo.member.domain.user.Role;
import my.todo.member.domain.user.User;

import java.util.Map;

public class OAuth2UserInfo {

    private Map<String, Object> attributes;
    private String platform;

    public OAuth2UserInfo(Map<String, Object> attributes, String platform){
        this.attributes = attributes;
        this.platform = platform;
    }

    public String getProviderId(){
        if(platform.equals("google")){
            return (String) attributes.get("sub");
        }
//        카카오 id가 Long 타입이기 때문에 (String) 타입 캐스팅 실패
//        String.valueOf() 로 수정
        return String.valueOf(attributes.get("id"));
    }

    public String getName(){
        return (String) attributes.get("name");
    }

    public String getEmail(){
        return (String) attributes.get("my/todo/email");
    }

    public String getPlatform(){
        return platform;
    }

    public User toEntity(){
        return User.builder()
                .username(platform+"_"+this.getProviderId())
                .email(this.getEmail())
                .role(Role.USER)
                .provider(platform)
                .providerId(this.getProviderId())
                .build();
    }
}
