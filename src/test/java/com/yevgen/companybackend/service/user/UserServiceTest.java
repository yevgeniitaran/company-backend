package com.yevgen.companybackend.service.user;

import com.yevgen.companybackend.jpa.CompanyRepository;
import com.yevgen.companybackend.jpa.UserRepository;
import com.yevgen.companybackend.model.Company;
import com.yevgen.companybackend.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String USERNAME = "123";
    private final String PASSWORD = "123";
    private final String COMPANY_NAME = "company";

    private final String EXISTED_USERNAME = "user";
    private final String EXISTED_USERNAME_UPDATED = "new";

    @Test
    public void registerNewUserTest() {
        Company company = companyRepository.findByCompanyName(COMPANY_NAME).iterator().next();
        User user = new User(USERNAME, PASSWORD, company);
        userService.registerNewUser(user);
        User foundUser = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(USERNAME, foundUser.getUsername());
        assertEquals(COMPANY_NAME, foundUser.getCompany().getCompanyName());
        assertTrue(passwordEncoder.matches(PASSWORD, user.getPassword()));
    }

    @Test
    public void updateUserTest() {
        User foundUser = userRepository.findByUsername(EXISTED_USERNAME).orElse(null);
        foundUser.setUsername(EXISTED_USERNAME_UPDATED);
        userService.updateUser(foundUser);
        assertTrue(userRepository.findByUsername(EXISTED_USERNAME_UPDATED).isPresent());
    }
}
