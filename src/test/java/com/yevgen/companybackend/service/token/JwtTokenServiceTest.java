package com.yevgen.companybackend.service.token;

import com.yevgen.companybackend.CompanyBackendApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CompanyBackendApplication.class)
@Import(TokenTestConfiguration.class)
public class JwtTokenServiceTest {

    @Autowired
    @Qualifier("jwtShortTokenService")
    private JwtTokenService jwtShortTokenService;

    @Autowired
    @Qualifier("jwtTokenService")
    private JwtTokenService jwtTokenService;

    private final String USERNAME_MAP_KEY = "username";
    private final String USERNAME = "admin";

    @Test
    public void tokenExpireOnTime() {
        Map<String, String> parsedToken = buildAndVerifyToken(jwtShortTokenService);
        assertNull(parsedToken.get(USERNAME_MAP_KEY));
    }

    @Test
    public void tokenGeneratedAndProcessedCorrectly() {
        Map<String, String> parsedToken = buildAndVerifyToken(jwtTokenService);
        assertEquals(USERNAME, parsedToken.get(USERNAME_MAP_KEY));
    }

    private Map<String,String> buildAndVerifyToken(JwtTokenService jwtTokenService) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(USERNAME_MAP_KEY,USERNAME);
        String token = jwtTokenService.expiring(attributes);
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jwtTokenService.verify(token);
    }
}