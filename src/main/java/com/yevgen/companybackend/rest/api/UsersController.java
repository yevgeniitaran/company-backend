package com.yevgen.companybackend.rest.api;


import com.fasterxml.jackson.annotation.JsonView;
import com.yevgen.companybackend.utils.jackson.Views;
import com.yevgen.companybackend.jpa.UserRepository;
import com.yevgen.companybackend.model.Company;
import com.yevgen.companybackend.model.User;
import com.yevgen.companybackend.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping("/v1/users")
public class UsersController {

    private UserRepository userRepository;
    private UserService userService;

    public UsersController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/current-user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @JsonView(Views.UserView.class)
    public User getCurrentUser(@AuthenticationPrincipal final User user) {
        return user;
    }

    @GetMapping("/current-user/company")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @JsonView(Views.CompanyView.class)
    public Company getUserCompany(@AuthenticationPrincipal final User user) {
        return user.getCompany();
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.UserView.class)
    public Collection<User> getUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.UserView.class)
    public User getUser(@PathVariable("id") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.UserView.class)
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        user.setId(id);
        user = userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/{id}", params = "company-id")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.UserView.class)
    public ResponseEntity<User> setCompany(@PathVariable("id") Long userId,
                                           @RequestParam("company-id") Long companyId) {
        return ResponseEntity.ok(userService.updateCompanyById(userId, companyId));
    }

}