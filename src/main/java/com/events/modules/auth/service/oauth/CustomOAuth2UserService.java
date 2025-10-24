package com.events.modules.auth.service.oauth;

import com.events.modules.auth.dto.RegisterCommand;
import com.events.modules.user.entity.User;
import com.events.modules.user.enumeration.RoleEnum;
import com.events.modules.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final IUserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Optional<User> exi  = userService.findByEmail(email);

        if(exi.isEmpty()) {
            String emptyString = "";

            RegisterCommand command = RegisterCommand.builder()
                    .fullName(name)
                    .email(email)
                    .password(emptyString)
                    .role(RoleEnum.USER)
                    .build();

            userService.createUser(command);
        }

        return oAuth2User;
    }
}
