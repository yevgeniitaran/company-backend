package com.yevgen.companybackend.service.auth;

import com.yevgen.companybackend.CompanyBackendApplication;
import com.yevgen.companybackend.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAuthenticationServiceTest {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    private final String ADMIN_USER = "admin";
    private final String ADMIN_USER_PASSWORD = "admin";

    @Test
    public void tokenVerifiesUserAfterUserLogin() {
        String token = userAuthenticationService.login(ADMIN_USER, ADMIN_USER_PASSWORD).orElse(null);
        Assert.assertNotNull(token);
        User user = userAuthenticationService.findByToken(token).orElse(null);
        Assert.assertNotNull(user);
        Assert.assertEquals("Token authentication doesn't return the same user after login", ADMIN_USER, user.getUsername());
    }
}