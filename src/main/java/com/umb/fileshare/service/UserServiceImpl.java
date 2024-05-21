package com.umb.fileshare.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.umb.fileshare.dto.UserSignupDto;
import com.umb.fileshare.model.EntityRole;
import com.umb.fileshare.model.Role;
import com.umb.fileshare.model.User;
import com.umb.fileshare.repository.RoleRepository;
import com.umb.fileshare.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserSignupDto userSignupDto) {
        // TODO: if role is not present then assign EntityRole.USER

        // TODO: handle errors

        Set<Role> roleSet = new HashSet<>();

        if (userSignupDto.getRole() == null) {
            Role userRole = roleRepository.findByName(EntityRole.USER).orElseThrow(
                    () -> NotFound.create(HttpStatusCode.valueOf(404), "Role not found", null, null, null));
            roleSet.add(userRole);
        } else {
            userSignupDto.getRole().forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(EntityRole.ADMIN).orElseThrow(
                                () -> NotFound.create(HttpStatusCode.valueOf(404), "Role not found", null, null, null));
                        roleSet.add(adminRole);
                        break;
                    case "god_user":
                        Role godUserRole = roleRepository.findByName(EntityRole.GOD_USER).orElseThrow(
                                () -> NotFound.create(HttpStatusCode.valueOf(404), "Role not found", null, null, null));
                        roleSet.add(godUserRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(EntityRole.USER).orElseThrow(
                                () -> NotFound.create(HttpStatusCode.valueOf(404), "Role not found", null, null, null));
                        roleSet.add(userRole);
                        break;
                }
            });
        }

        User user = new User(userSignupDto.getFirstName(), userSignupDto.getLastName(), userSignupDto.getEmail(),
                userSignupDto.getUsername(), userSignupDto.getPassword(), roleSet, null);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserByEmailOrUsername(String usernameEmail) {
        if (usernameEmail.contains("@")) {
            return userRepository.findByEmail(usernameEmail);
        }

        return userRepository.findByUsername(usernameEmail);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Role> findByName(EntityRole name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Optional<Role> findById(@NonNull Long id) {
        return roleRepository.findById(id);
    }
}
