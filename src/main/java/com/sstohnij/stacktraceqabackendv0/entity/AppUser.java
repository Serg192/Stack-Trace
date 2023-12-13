package com.sstohnij.stacktraceqabackendv0.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private int rating;

    private boolean emailVerified;

    private boolean accountDeleted;

    private boolean accountBanned;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "app_users_roles", joinColumns = {
            @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")
    })
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountDeleted && !accountBanned;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountDeleted && !accountBanned;

    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !accountDeleted && !accountBanned;
    }

    @Override
    public boolean isEnabled() {
        return emailVerified && !accountDeleted && !accountBanned;
    }
}
