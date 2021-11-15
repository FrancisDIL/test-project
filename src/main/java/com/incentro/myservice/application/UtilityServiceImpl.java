package com.incentro.myservice.application;

import com.incentro.myservice.application.config.constant.Oauth2Configurations;
import com.incentro.myservice.application.exception.AppInputErrorException;
import com.incentro.myservice.users.entity.User;
import com.incentro.myservice.users.entity.UserPasswordReset;
import com.incentro.myservice.users.respository.UserPasswordResetRepository;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilityServiceImpl implements UtilityService {

    private UserPasswordResetRepository userPasswordResetRepository;

    public UtilityServiceImpl(UserPasswordResetRepository userPasswordResetRepository) {
        this.userPasswordResetRepository = userPasswordResetRepository;
    }

    @Override
    public String createResetPasswordToken(User user) {
        UserPasswordReset userPasswordReset = new UserPasswordReset();
        userPasswordReset.setExpiryDate(LocalDate.now().plusDays(Oauth2Configurations.PASSWORD_RESET_TOKEN_DURATION));
        userPasswordReset.setUser(user);
        userPasswordReset.setToken(randomStringGenerator(150));
        UserPasswordReset savedUserPasswordReset = userPasswordResetRepository.save(userPasswordReset);
        return savedUserPasswordReset.getToken();
    }

    @Override
    public String createResetPasswordToken(User user, LocalDate expiryDate) {
        UserPasswordReset userPasswordReset = new UserPasswordReset();
        userPasswordReset.setExpiryDate(expiryDate.plusDays(Oauth2Configurations.PASSWORD_RESET_TOKEN_DURATION));
        userPasswordReset.setUser(user);
        userPasswordReset.setToken(randomStringGenerator(150));
        UserPasswordReset savedUserPasswordReset = userPasswordResetRepository.save(userPasswordReset);
        return savedUserPasswordReset.getToken();
    }

    @Override
    public String randomStringGenerator(int characterSize) {
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
                .build();
        return randomStringGenerator.generate(characterSize);
    }

    @Override
    public String validateAndGetFileExtensionForProfileImage(String fileName) {
        String[] allowedExt = {".png", ".jpg", "jpeg"};
        if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
            for (String ext : allowedExt) {
                if (fileName.toLowerCase().trim().endsWith(ext)) {
                    return ext;
                }
            }
        }
        throw new AppInputErrorException("error.upload-unsupported-file-format", Arrays.asList(allowedExt));
    }

    @Override
    public String emmusInCommaSeparatedList(List<String> strings) {
        return strings.stream()
                .sorted()
                .collect(Collectors.joining(","));
    }
}
