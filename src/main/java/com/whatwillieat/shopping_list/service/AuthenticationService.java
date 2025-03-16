package com.whatwillieat.shopping_list.service;

import com.whatwillieat.shopping_list.configuration.AuthConfig;
import com.whatwillieat.shopping_list.security.AuthenticationMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    public static Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(AuthConfig.getAuthToken())) {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new AuthenticationMapper(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
