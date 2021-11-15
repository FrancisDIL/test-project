package com.incentro.myservice.application;

import com.incentro.myservice.users.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface UtilityService {

    String createResetPasswordToken(User user);

    String createResetPasswordToken(User user, LocalDate expiryDate);

    String randomStringGenerator(int characterSize);

    String validateAndGetFileExtensionForProfileImage(String fileName);

    String emmusInCommaSeparatedList(List<String> strings);
}
