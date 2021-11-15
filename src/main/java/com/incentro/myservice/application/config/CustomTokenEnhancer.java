package com.incentro.myservice.application.config;

import com.incentro.myservice.users.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            User user = (User) authentication.getPrincipal();
            Map<String, Object> additionalInfo = new HashMap<>();

            additionalInfo.put("firstName", user.getFirstName());
            additionalInfo.put("lastName", user.getLastName());
            additionalInfo.put("phoneNumber", user.getPhoneNumber());
            additionalInfo.put("role_titles", user.getRole());

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        }

        return accessToken;
    }

}
