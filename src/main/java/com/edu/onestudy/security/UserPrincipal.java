package com.edu.onestudy.security;

import com.edu.onestudy.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Slf4j
@ToString
public class UserPrincipal implements UserDetails {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private List<String> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
    public UserPrincipal(UUID id, String username, String email, String password, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public static UserPrincipal from(User user) {
        return UserPrincipal.builder()
                .id(UUID.fromString(user.getId().toString()))
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getCredentials().getPasswordHash())
                .roles(List.of(user.getRole().toString()))
                .build();
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
