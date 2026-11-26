package com.example.ssmstudy.controller;


import com.example.ssmstudy.dto.CurrentUserResponse;
import com.example.ssmstudy.dto.RegisterRequest;
import com.example.ssmstudy.dto.RegisterResponse;
import com.example.ssmstudy.model.User;
import com.example.ssmstudy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request){
        User user = userService.register(request.getUsername(), request.getPassword());
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(user.getId());
        registerResponse.setUsername(user.getUsername());
        return registerResponse;
    }

    @GetMapping("/me")
    public CurrentUserResponse me(
            @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        User user = userService.getCurrentUser(username);
        CurrentUserResponse currentUserResponse = new CurrentUserResponse(user.getId(), user.getUsername());
        return currentUserResponse;
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken csrfToken){
        return csrfToken;
    }
}
