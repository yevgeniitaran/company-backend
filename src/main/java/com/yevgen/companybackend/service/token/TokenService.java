package com.yevgen.companybackend.service.token;

import java.util.Map;

/**
 * Creates and validates credentials.
 */
public interface TokenService {

    String expiring(Map<String, String> attributes);

    Map<String, String> verify(String token);
}