package com.potluck.buffet.helpers;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class GenerateSecretKey {
    public static void main(String[] args) {
        for(int i=0 ; i<1001; i++) {
            String key = new String(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
            System.out.println("key" + key);
        }
    }
}
