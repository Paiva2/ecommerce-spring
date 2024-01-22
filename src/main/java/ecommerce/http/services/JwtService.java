package ecommerce.http.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import ecommerce.http.entities.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Client client) {
        String subjectId = client.getId().toString();

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create().withIssuer("ecommerce-api").withSubject(subjectId)
                    .withExpiresAt(generateExpirationTime()).sign(algorithm);

            return token;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error while generating token...", exception);
        }
    }

    private Instant generateExpirationTime() {
        ZoneOffset GMT_OFFSET = ZoneOffset.of("+0");

        return LocalDateTime.now().plusHours(2).toInstant(GMT_OFFSET);
    }
}
