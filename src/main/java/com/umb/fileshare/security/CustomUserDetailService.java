package com.umb.fileshare.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.umb.fileshare.model.User;
import com.umb.fileshare.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByEmailOrUsername(username).orElseThrow();

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();

        user.getRoles().forEach(role -> simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.toString())));

        return UserPrincipal.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(simpleGrantedAuthorities)
                .build();
    }

}
