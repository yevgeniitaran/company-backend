package com.yevgen.companybackend.service.auth;

import com.yevgen.companybackend.jpa.UserRepository;
import com.yevgen.companybackend.model.User;
import com.yevgen.companybackend.service.token.TokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TokenAuthenticationService implements UserAuthenticationService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public TokenAuthenticationService(@Qualifier("jwtTokenService") TokenService tokenService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<String> login(final String username, final String password) {
        return userRepository
                .findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("username", username);
                    return tokenService.expiring(Collections.unmodifiableMap(map));
                });
    }

    @Override
    public Optional<User> findByToken(final String token) {
        return Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(userRepository::findByUsername);
    }

    @Override
    public void logout(final User user) {
        // TODO: Implement token expiration
    }
}