package com.umb.fileshare.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.umb.fileshare.exchanges.request.LoginRequest;
import com.umb.fileshare.exchanges.response.LoginResponse;
import com.umb.fileshare.security.JwtIssuer;
import com.umb.fileshare.security.UserPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class AuthController {
    private final JwtIssuer jwtIssuer;

    private final AuthenticationManager authenticationManager;

    @PostMapping("api/auth/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest request) {
        // TODO: move this to service layer
        // TODO: add specific error whether username or password is invalid
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

            String token = jwtIssuer.issue(principal.getUserId(), request.getUsername(), roles);

            return ResponseEntity.ok(LoginResponse.builder().accessToken(token).build());
        } catch (BadCredentialsException e) {
            log.debug("Invalid username or password", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (InternalAuthenticationServiceException e) {
            log.error("Authentication service error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal authentication service error");
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed");
        }

    }
}
