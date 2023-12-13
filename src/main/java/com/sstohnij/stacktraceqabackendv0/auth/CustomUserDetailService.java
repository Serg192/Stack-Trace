package com.sstohnij.stacktraceqabackendv0.auth;

import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username: '%s' not found", username)));

        try {
            new AccountStatusUserDetailsChecker().check(appUser);
        } catch(AccountStatusException e){
            log.error("Could not authenticate user", e);
            throw new RuntimeException(e.getMessage());
        }
        return appUser;
    }
}
