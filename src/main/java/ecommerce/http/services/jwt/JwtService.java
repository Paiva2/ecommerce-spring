package ecommerce.http.services.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import ecommerce.http.entities.Client;
import ecommerce.http.exceptions.BadRequestException;

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

            System.out.println(generateExpirationTime());

            String token = JWT.create().withIssuer("ecommerce-api").withSubject(subjectId)
                    .withExpiresAt(generateExpirationTime()).sign(algorithm);

            return token;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error while generating token...", exception);
        }
    }

    public String validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            JWTVerifier verifier = JWT.require(algorithm).withIssuer("ecommerce-api").build();

            return verifier.verify(token.trim()).getSubject().toString();
        } catch (JWTVerificationException exception) {
            throw new BadRequestException(exception.toString());
        }
    }

    private Instant generateExpirationTime() {
        ZoneOffset OFFSET = ZoneOffset.of("+0"); // GMT

        return LocalDateTime.now().plusHours(10).toInstant(OFFSET);
    }
}
