package com.potluck.buffet.security;

public interface SecurityConstants {
    //String SECRET = "SecretKeyToGenJWTs";
    String SECRET = "PotLuckKeyToGen";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SIGN_UP_URL = "/users/sign-up";
    long EXPIRATION_TIME = 864_000_000; // 10 days
}
