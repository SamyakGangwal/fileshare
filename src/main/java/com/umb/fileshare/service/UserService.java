package com.umb.fileshare.service;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.umb.fileshare.dto.UserSignupDto;
import com.umb.fileshare.model.EntityRole;
import com.umb.fileshare.model.Role;
import com.umb.fileshare.model.User;

@Service
public interface UserService {
    public User save(UserSignupDto userSignupDto);

    public Optional<User> findUserByEmail(String email);

    public Optional<User> findUserByEmailOrUsername(String usernameEmail);

    public Optional<User> findUserByUsername(String username);

    public List<User> findAllUsers();

    public Optional<Role> findByName(EntityRole name);

    public Optional<Role> findById(@NonNull Long id);
}
