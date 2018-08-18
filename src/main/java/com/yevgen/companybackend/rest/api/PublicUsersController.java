package com.yevgen.companybackend.rest.api;

import com.yevgen.companybackend.model.User;
import com.yevgen.companybackend.service.auth.UserAuthenticationService;
import com.yevgen.companybackend.service.user.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/public/users")
public class PublicUsersController {

    private UserAuthenticationService userAuthenticationService;
    private UserService userService;

    public PublicUsersController(UserAuthenticationService userAuthenticationService, UserService userService) {
        this.userAuthenticationService = userAuthenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerNewUser(user);
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        return userAuthenticationService
                .login(username, password)
                .orElseThrow(() -> new BadCredentialsException("invalid login and/or password") {
                });
    }
}