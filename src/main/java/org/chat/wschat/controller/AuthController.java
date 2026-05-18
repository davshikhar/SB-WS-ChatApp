package org.chat.wschat.controller;

import jakarta.validation.Valid;
import org.chat.wschat.DTO.LoginRequest;
import org.chat.wschat.DTO.Request;
import org.chat.wschat.Security.JwtUtil;
import org.chat.wschat.Service.UserService;
import org.chat.wschat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Request req){
        User user = userService.resgisterUser(req.getUsername(), req.getEmail(), req.getPassword());
        return ResponseEntity.ok(Map.of(
                "username",user.getUsername(),
        "message","User registered successfully"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest req){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        UserDetails userDetails = userService.loadUserByUsername(req.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(Map.of(
                "token",token,
                "message","User logged in successfully",
                "type","Bearer"
        ));
    }
}
