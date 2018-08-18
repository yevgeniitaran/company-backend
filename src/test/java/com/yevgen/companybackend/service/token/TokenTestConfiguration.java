package com.yevgen.companybackend.service.token;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenTestConfiguration {
    @Bean
    JwtTokenService jwtShortTokenService() {
        return new JwtTokenService("test", 1, "secret");
    }
}
