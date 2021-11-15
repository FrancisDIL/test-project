package com.incentro.myservice.application.config;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Transactional
public class OAuthTokenServices extends DefaultTokenServices {

    private static final Logger LOGGER = Logger.getLogger(OAuthTokenServices.class.getName());

    @Override
    @Retryable(include = DuplicateKeyException.class)
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        try {
            return super.createAccessToken(authentication);
        }
        catch (AuthenticationException ex) {
            LOGGER.info(String.format(
                    "AuthenticationException while creating access token %s", ex));
            throw ex;
        }
        catch (DuplicateKeyException ex) {
            LOGGER.info(String
                    .format("DuplicateKeyException while creating access token %s", ex));
            throw ex;
        }
        catch (Exception ex) {
            LOGGER.info(String.format("Exception while creating access token %s", ex));
            throw new RuntimeException(ex);
        }
    }

    @Recover
    public OAuth2AccessToken recoverAccessToken(OAuth2Authentication authentication)
            throws AuthenticationException {
        try {
            return getAccessToken(authentication);
        }
        catch (Exception ex) {
            LOGGER.info(String.format("Exception while creating access token %s", ex));
        }
        throw new IllegalStateException("Cannot create access token");
    }
}
