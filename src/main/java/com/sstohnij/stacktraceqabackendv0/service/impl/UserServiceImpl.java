package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.dto.request.UserRegistrationRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.UserResponse;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Role;
import com.sstohnij.stacktraceqabackendv0.enums.RoleName;
import com.sstohnij.stacktraceqabackendv0.mapper.UserMapper;
import com.sstohnij.stacktraceqabackendv0.repository.AppUserRepository;
import com.sstohnij.stacktraceqabackendv0.repository.RoleRepository;
import com.sstohnij.stacktraceqabackendv0.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
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

        //JUST FOR TEST PURPOSES
        user.setEmailVerified(true);
        ///

        user = appUserRepository.save(user);
        // send email confirmation
        return UserMapper.toUserResponse(user);
    }
}
