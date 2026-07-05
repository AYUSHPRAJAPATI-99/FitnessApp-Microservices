package com.fitness.userservice.util;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class SecretKeyGenerator {

    public static void main(String[] args) {

        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

        String secret = Encoders.BASE64.encode(key.getEncoded());

        System.out.println(secret);
    }
}