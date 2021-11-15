package com.incentro.myservice.oauth.service;

import com.incentro.myservice.application.UtilityService;
import com.incentro.myservice.application.config.constant.Oauth2Configurations;
import com.incentro.myservice.application.exception.AppInputErrorException;
import com.incentro.myservice.oauth.dto.ChangePasswordRequest;
import com.incentro.myservice.oauth.dto.ForgotPasswordRequest;
import com.incentro.myservice.oauth.dto.TokenChangePasswordRequest;
import com.incentro.myservice.users.entity.User;
import com.incentro.myservice.users.entity.UserPasswordReset;
import com.incentro.myservice.users.respository.UserPasswordResetRepository;
import com.incentro.myservice.users.respository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional
public class OauthServiceImpl implements OauthService {

    @Resource(name = "tokenStore")
    TokenStore tokenStore;

    private DefaultTokenServices tokenServices;
    private UtilityService utilityService;
    private UserRepository userRepository;
    private UserPasswordResetRepository userPasswordResetRepository;

    public OauthServiceImpl(DefaultTokenServices tokenServices, UtilityService utilityService, UserRepository userRepository, UserPasswordResetRepository userPasswordResetRepository) {
        this.tokenServices = tokenServices;
        this.utilityService = utilityService;
        this.userRepository = userRepository;
        this.userPasswordResetRepository = userPasswordResetRepository;
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmailAndAccountDeletedFalse(forgotPasswordRequest.getEmail()).orElseThrow(() -> new AppInputErrorException("oauth.password.reset.invalid.username"));

        if (user.isEnabled()) {
            String resetToken = utilityService.createResetPasswordToken(user);

            //send email

        } else {
            throw new AppInputErrorException("user.disabled");
        }

    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        final OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((User) authentication.getPrincipal()).getUsername();
        } else {
            username = (String) principal;
        }

        Optional<User> userOptional = userRepository.findByEmailAndAccountDeletedFalse(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isEnabled()) {
                if (!user.getUsername().equalsIgnoreCase(changePasswordRequest.getEmail())) {
                    throw new AppInputErrorException("oauth.invalid.email");
                }
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                if (bCryptPasswordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                    user.setPassword(new BCryptPasswordEncoder().encode(changePasswordRequest.getPassword()));
                    userRepository.save(user);
                } else {
                    throw new AppInputErrorException("oauth.invalid.old.password");
                }
            } else {
                throw new AppInputErrorException("user.disabled");
            }
        } else {
            throw new AppInputErrorException("oauth.invalid.token");
        }
    }

    @Override
    public void resetPasswordByToken(TokenChangePasswordRequest tokenChangePasswordRequest) {
        Optional<UserPasswordReset> userPasswordResetOptional = userPasswordResetRepository.findByToken(tokenChangePasswordRequest.getToken());
        if (userPasswordResetOptional.isPresent()) {
            UserPasswordReset userPasswordReset = userPasswordResetOptional.get();
            LocalDate now = LocalDate.now();
            LocalDate expiryDate = userPasswordReset.getExpiryDate();

            long daysBetween = DAYS.between(expiryDate, now);
            if (daysBetween > 0) {
                userPasswordResetRepository.delete(userPasswordReset);
                throw new AppInputErrorException("oauth.reset.token.expired");
            }
            User user = userPasswordReset.getUser();
            if (user.isEnabled()) {
                user.setPassword(new BCryptPasswordEncoder().encode(tokenChangePasswordRequest.getPassword()));
                userRepository.save(user);
            } else {
                throw new AppInputErrorException("user.disabled");
            }
            userPasswordResetRepository.delete(userPasswordReset);
        } else {
            throw new AppInputErrorException("oauth.invalid.reset.token");
        }
    }

    @Override
    public Boolean userHasRole(User user, User.Role role) {
        if (user.getRole().contains(role)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean userHasRoleThrowsError(User user, User.Role role) {
        if (user.getRole().contains(role)) {
            return Boolean.TRUE;
        }
        throw new AppInputErrorException("user.does.not.have.role", user.getFirstName(), user.getLastName(), role.getAuthority());
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByEmailAndAccountDeletedFalse(username).orElseThrow(() -> new AppInputErrorException("user.does.not.exist"));
    }

    @Override
    public User getLoggedInUser() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2Authentication) {
            final OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if (principal instanceof String) {
                return getUserByUsername((String) principal);
            } else { //UserDetails
                return getUserByUsername(((User) authentication.getPrincipal()).getUsername());
            }
        } else if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            return getUserByUsername(userDetails.getUsername());
        }
        throw new AppInputErrorException("user.does.not.exist");
    }

    public String createResetPasswordToken(User user) {
        UserPasswordReset userPasswordReset = new UserPasswordReset();
        userPasswordReset.setExpiryDate(LocalDate.now().plusDays(Oauth2Configurations.PASSWORD_RESET_TOKEN_DURATION));
        userPasswordReset.setUser(user);
        userPasswordReset.setToken(utilityService.randomStringGenerator(150));
        UserPasswordReset savedUserPasswordReset = userPasswordResetRepository.save(userPasswordReset);
        return savedUserPasswordReset.getToken();
    }

}
