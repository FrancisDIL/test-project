package com.incentro.myservice.application.config;

import com.incentro.myservice.users.entity.User;
import com.incentro.myservice.users.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "createAuditorProvider")
@Configuration
public class AuditingConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public AuditorAware<User> createAuditorProvider() {
        return new AuditorAware<User>() {
            @Override
            public Optional<User> getCurrentAuditor() {
                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2Authentication) {
                        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
                        Object principal = auth.getUserAuthentication().getPrincipal();
                        if (principal instanceof UserDetails) {
                            UserDetails userDetails = (UserDetails) principal;
                            return userRepository.findByEmail(userDetails.getUsername());
                        } else {
                            return userRepository.findByEmail((String) principal);
                        }
                    } else {
                        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                        Object principal = auth.getPrincipal();
                        UserDetails userDetails = (UserDetails) principal;
                        return userRepository.findByEmail(userDetails.getUsername());
                    }
                } else {
                    return Optional.empty();
                }
            }
        };
    }
}
