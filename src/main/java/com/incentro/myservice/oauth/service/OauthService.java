package com.incentro.myservice.oauth.service;

import com.incentro.myservice.oauth.dto.ChangePasswordRequest;
import com.incentro.myservice.oauth.dto.ForgotPasswordRequest;
import com.incentro.myservice.oauth.dto.TokenChangePasswordRequest;
import com.incentro.myservice.users.entity.User;

public interface OauthService {

    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    void changePassword(ChangePasswordRequest changePasswordRequest);

    void resetPasswordByToken(TokenChangePasswordRequest tokenChangePasswordRequest);

    Boolean userHasRoleThrowsError(User user, User.Role role);

    User getUserByUsername(String username);

    User getLoggedInUser();

    Boolean userHasRole(User user, User.Role role);
}
