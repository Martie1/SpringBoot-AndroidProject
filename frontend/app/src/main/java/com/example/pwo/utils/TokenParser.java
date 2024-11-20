package com.example.pwo.utils;

import com.auth0.android.jwt.JWT;

public class TokenParser {
    public static String extractRole(String accessToken) {
        try {
            JWT jwt = new JWT(accessToken);


            String[] roles = jwt.getClaim("roles").asArray(String.class);


            if (roles != null && roles.length > 0) {
                return roles[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
