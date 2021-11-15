package com.incentro.myservice.users.service;

import com.incentro.myservice.application.UtilityService;
import com.incentro.myservice.application.exception.AppInputErrorException;
import com.incentro.myservice.oauth.service.OauthService;
import com.incentro.myservice.users.entity.User;
import com.incentro.myservice.users.respository.UserRepository;
import com.incentro.myservice.users.dto.AddUserDTO;
import com.incentro.myservice.users.dto.EditUserDTO;
import com.incentro.myservice.users.dto.UserResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final OauthService oauthService;
    private final UserRepository userRepository;
    private final UtilityService utilityService;

    public UsersServiceImpl(OauthService oauthService, UserRepository userRepository, UtilityService utilityService) {
        this.oauthService = oauthService;
        this.userRepository = userRepository;
        this.utilityService = utilityService;
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public UserResponseDTO addUser(AddUserDTO addUserDTO) {
        boolean emailExist = userRepository.existsByEmailAndAccountDeletedFalse(addUserDTO.getEmail(), 0L);
        if (emailExist) {
            throw new AppInputErrorException("user.email.exist");
        }
        boolean phoneNumberExist = userRepository.existsByPhoneNumberAndAccountDeletedFalse(addUserDTO.getPhoneNumber(), 0L);
        if (phoneNumberExist) {
            throw new AppInputErrorException("user.phone.number.exist");
        }

        User user = new User();
        user.setCreatedBy(oauthService.getLoggedInUser());
        user.setCreatedDate(LocalDateTime.now());
        user.setEmail(addUserDTO.getEmail());
        user.setFirstName(addUserDTO.getFirstName());
        user.setLastName(addUserDTO.getLastName());
        user.setPhoneNumber(addUserDTO.getPhoneNumber());
        user.setEnabled(addUserDTO.isActive());
        user.setAccountDeleted(Boolean.FALSE);
        user.addRole(addUserDTO.getRole());
        user.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));

        user.setPassword(new BCryptPasswordEncoder().encode(utilityService.randomStringGenerator(25)));
        User savedUser = userRepository.save(user);

        //create a reset password token
        String resetToken = utilityService.createResetPasswordToken(savedUser);

        //send email

        return savedUser.toUserResponse();
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public UserResponseDTO editUser(EditUserDTO editUserDTO) {
        User user = userRepository.findByIdAndAccountDeletedFalse(editUserDTO.getId()).orElseThrow(() -> new AppInputErrorException("user.does.not.exist"));

        if (userRepository.existsByEmailAndAccountDeletedFalse(editUserDTO.getEmail(), user.getId())) {
            throw new AppInputErrorException("user.email.exist");
        }
        if (userRepository.existsByPhoneNumberAndAccountDeletedFalse(editUserDTO.getPhoneNumber(), user.getId())) {
            throw new AppInputErrorException("user.phone.number.exist");
        }

        user.setLastModifiedBy(oauthService.getLoggedInUser());
        user.setLastModifiedDate(LocalDateTime.now());
        user.setEmail(editUserDTO.getEmail());
        user.setFirstName(editUserDTO.getFirstName());
        user.setLastName(editUserDTO.getLastName());
        user.setPhoneNumber(editUserDTO.getPhoneNumber());
        user.setEnabled(editUserDTO.isActive());
        user.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));

        return userRepository.save(user).toUserResponse();
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public void deleteUser(Long userId) {
        final User adminUser = oauthService.getLoggedInUser();

        User user = userRepository.findByIdAndAccountDeletedFalse(userId).orElseThrow(() -> new AppInputErrorException("user.does.not.exist"));
        user.setAccountDeleted(Boolean.TRUE);
        user.setAccountDeletedDate(LocalDateTime.now());
        user.setAccountDeletedBy(adminUser);
        user.setEnabled(Boolean.FALSE);

        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public UserResponseDTO getUserById(Long userId) {
        return userRepository.findByIdAndAccountDeletedFalse(userId).orElseThrow(() -> new AppInputErrorException("user.does.not.exist")).toUserResponse();
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public List<UserResponseDTO> getUsers() {
        return userRepository.getAllUsersAccountDeletedFalse().stream().map(user -> user.toUserResponse()).collect(Collectors.toList());
    }
}
