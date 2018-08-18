package com.yevgen.companybackend.service.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;

@Service
public class JwtTokenService implements TokenService {

    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    private String issuer;
    private int expirationMilliseconds;
    private String secretKey;

    JwtTokenService(@Value("${jwt.issuer:backend-company}") final String issuer,
                    @Value("${jwt.expiration-milliseconds:86400000}") final int expirationMilliseconds,
                    @Value("${jwt.secret:secret}") final String secret) {
        super();
        this.issuer = requireNonNull(issuer);
        this.expirationMilliseconds = expirationMilliseconds;
        this.secretKey = BASE64.encode(requireNonNull(secret));
    }

    @Override
    public String expiring(final Map<String, String> attributes) {
        return newToken(attributes, expirationMilliseconds);
    }

    private String newToken(final Map<String, String> attributes, final int expiresInSec) {
        Date now = new Date();
        final Claims claims = Jwts
                .claims()
                .setIssuer(issuer)
                .setIssuedAt(now);

        if (expiresInSec > 0) {
            claims.setExpiration(new Date(now.getTime() + expirationMilliseconds));
        }
        claims.putAll(attributes);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    @Override
    public Map<String, String> verify(final String token) {
        final JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setSigningKey(secretKey);
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
        try {
            final Claims claims = toClaims.get();
            final Map<String, String> map = new HashMap<>();
            for (final Map.Entry<String, Object> e: claims.entrySet()) {
                map.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return Collections.unmodifiableMap(map);
        } catch (final IllegalArgumentException | JwtException e) {
            return new HashMap<>();
        }
    }
}