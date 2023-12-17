package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.dto.request.UserRegistrationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.UserResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.UsersPageResponse;
import com.sstohnij.stacktraceqabackendv0.email.template.EmailTemplateType;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Role;
import com.sstohnij.stacktraceqabackendv0.enums.RoleName;
import com.sstohnij.stacktraceqabackendv0.mapper.UserMapper;
import com.sstohnij.stacktraceqabackendv0.repository.AppUserRepository;
import com.sstohnij.stacktraceqabackendv0.repository.RoleRepository;
import com.sstohnij.stacktraceqabackendv0.email.EmailSender;
import com.sstohnij.stacktraceqabackendv0.service.JwtTokenService;
import com.sstohnij.stacktraceqabackendv0.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtTokenService jwtTokenService;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        final String username = userRegistrationRequest.getUsername();
        final String email = userRegistrationRequest.getEmail();
        final String defaultRole = RoleName.ROLE_USER.str();

        if(appUserRepository.findByUsername(username).isPresent())
            throw new EntityExistsException(String.format("User with username: '%s' already exist", username));

        if(appUserRepository.findByEmail(email).isPresent())
            throw new EntityExistsException(String.format("User with email: '%s' already exist", email));

        Role role = roleRepository
                .findByRoleName(defaultRole)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Role: '%s' is not present in database", defaultRole)
                ));

        userRegistrationRequest.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));

        AppUser user = UserMapper.fromRegistrationRequest(userRegistrationRequest);
        user.setRoles(Set.of(role));
        user = appUserRepository.save(user);

        final String emailConfirmationToken = jwtTokenService.generateConfirmationToken(user);
        final String confirmationLink = "http://localhost:8080/api/v0/auth/email-confirmation?token=" + emailConfirmationToken;

        Context templateContext = EmailTemplateType.EMAIL_CONFIRMATION.getDefaultTemplateContext();
        templateContext.setVariable("link", confirmationLink);

        emailSender.sendEmailUsingTemplate(
                userRegistrationRequest.getEmail(),
                "Confirmation",
                EmailTemplateType.EMAIL_CONFIRMATION,
                templateContext
        );
        return UserMapper.toUserResponse(user);
    }

    @Override
    public UsersPageResponse getAllUsers(int page, int pageSize) {
        Page<AppUser> users = appUserRepository.findAllByOrderByRatingDesc(PageRequest.of(page, pageSize));
        List<UserResponse> result = users.stream().map(UserMapper::toUserResponse).toList();
        return UsersPageResponse.builder()
                .currentPage(users.getNumber())
                .totalPages(users.getTotalPages())
                .users(result)
                .build();
    }
}
