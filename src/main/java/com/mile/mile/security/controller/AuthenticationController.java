package com.mile.mile.security.controller;

import com.mile.mile.security.entity.UserEntity;
import com.mile.mile.security.model.TokenModel;
import com.mile.mile.security.model.UserModel;
import com.mile.mile.security.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/login")
    public TokenModel login(@RequestBody UserModel userModel) {
        authenticate(userModel.getUsername(), userModel.getPassword());
        return userDetailsService.login(userModel.getUsername());
    }

    @PostMapping(value = "/register")
    public UserEntity saveUser(@RequestBody UserModel user) {
        return userDetailsService.save(user);
    }

    @PostMapping(value = "/refresh")
    public TokenModel tokenPostRefresh(@RequestBody TokenModel refreshToken) {
        return userDetailsService.refreshAccessToken(refreshToken.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not valid!"));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Object> logout(@RequestBody TokenModel refreshToken) {
        userDetailsService.logoutUser(refreshToken.getRefreshToken());
        return ResponseEntity.ok().build();
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
