package com.edatasite.workforce.rest.v3.release10.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AppleTokenValidator {
    private final Cache<String, PublicKey> publicKeyCache = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public boolean verifyAppleToken(String jwtToken, String clientId) {
        try {
            String kid = extractKeyIdFromJwt(jwtToken);

            PublicKey publicKey = publicKeyCache.get(kid, () -> fetchApplePublicKey(kid));

            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            if (!"https://appleid.apple.com".equals(claims.getIssuer())) {
                throw new Exception("Invalid token issuer");
            }
            if (!clientId.equals(claims.getAudience())) {
                throw new Exception("Invalid token audience");
            }
            if (claims.getExpiration().before(new Date())) {
                throw new Exception("Token expired");
            }

            String userId = claims.getSubject();

            return true;

        } catch (SignatureException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private PublicKey fetchApplePublicKey(String kid) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, List<Map<String, String>>> publicKeys = restTemplate.getForObject(APPLE_PUBLIC_KEYS_URL, Map.class);
        Map<String, String> keyDetails = (Map<String, String>) publicKeys.get("keys").stream().filter(e -> kid.equals(e.get("kid"))).findFirst().orElse(new HashMap<>());

        byte[] nBytes = Base64.getUrlDecoder().decode(keyDetails.get("n"));
        byte[] eBytes = Base64.getUrlDecoder().decode(keyDetails.get("e"));

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));
        return KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
    }

    private String extractKeyIdFromJwt(String jwtToken) throws Exception {
        String[] tokenParts = jwtToken.split("\\.");
        String header = new String(Base64.getUrlDecoder().decode(tokenParts[0]));
        Map<String, String> headerMap = objectMapper.readValue(header, Map.class);
        return headerMap.get("kid");
    }
}
