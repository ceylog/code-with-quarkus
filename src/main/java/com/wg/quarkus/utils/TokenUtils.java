package com.wg.quarkus.utils;

import io.smallrye.jwt.build.Jwt;
import java.util.Set;

public class TokenUtils {

    public static String generateJWTToken(String username, Set<String> roles, Long duration, String issuer){
        String token = Jwt.issuer(issuer).subject(username)
                .groups(roles)
                .expiresAt(currentTimeInSecs() + duration)
                .sign();
        return token;
    }

    public static int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }

}