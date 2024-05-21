package com.umb.fileshare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.umb.fileshare.dto.UserSignupDto;
import com.umb.fileshare.exchanges.response.MessageResponse;
import com.umb.fileshare.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
// @RequestMapping("api/auth")
public class UserRegistration {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("api/auth/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupDto signUpDto) {
        if (userService.findUserByUsername(signUpDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.findUserByEmail(signUpDto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        signUpDto.setPassword(new BCryptPasswordEncoder().encode(signUpDto.getPassword()));
        
        // TODO: add verification step and send OTP to the user

        // TODO: log the user in and send the user token
        userService.save(signUpDto);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    // @PostMapping("/signout")
    // public ResponseEntity<?> logoutUser() {
    //     ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    //     return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
    //             .body(new MessageResponse("You've been signed out!"));
    // }

}
