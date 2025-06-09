package br.com.comigo.autenticador.infrastructure.security;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.comigo.autenticador.domain.User;
import br.com.comigo.autenticador.domain.Role.Status;

@Component
public class JwtUtils 
{
    final private Logger logger = LoggerFactory.getLogger(getClass());

    static final String issuer = "Comigo Auth-Service";

    private long accessTokenExpirationMs;

    private Algorithm accessTokenAlgorithm;
    //private Algorithm refreshTokenAlgorithm;
    private JWTVerifier accessTokenVerifier;
    //private JWTVerifier refreshTokenVerifier;

    public JwtUtils(@Value("${yc.security.keys.accessToken.secret}") String accessTokenSecret, 
            @Value("${yc.security.keys.accessToken.expirationMinutes}") int accessTokenExpirationMinutes) {
        accessTokenExpirationMs = (long) accessTokenExpirationMinutes * 60 * 1000;
        accessTokenAlgorithm = Algorithm.HMAC512(accessTokenSecret);
        accessTokenVerifier = JWT.require(accessTokenAlgorithm)
            .withIssuer(issuer)
            .build();
        /*refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
            .withIssuer(issuer)
            .build();*/
    }

    public String generateAccessToken(User user) {
        JSONArray roles = new JSONArray();
        JSONArray authorities = new JSONArray();

        user.getRoles().forEach(role -> {
            if (role.getName() == null) {
                new RuntimeException("Há uma instancia de papel definido como ".concat(role.getName()));
            }
            
            if (role.getStatus() == null) {
                new RuntimeException("O papel ".concat(role.getName()).concat(" parece não ter um status definido."));
            } else if (role.getStatus() == Status.ACTIVE) {

            } else {

            }

            roles.put(role.getName());

            authorities.put(role.getName());
        });

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + accessTokenExpirationMs))
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail().email())
                .withClaim("status", user.getStatus().name())
                .withClaim("roles", roles.toString())
                .withClaim("authorities", authorities.toString())
                .sign(accessTokenAlgorithm);
    }

    private Optional<DecodedJWT> decodeAccessToken(String token) {
        try {
            return Optional.of(accessTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            logger.error("invalid access token", e);
        }
        return Optional.empty();
    }

    public boolean validateAccessToken(String token) {
        return decodeAccessToken(token).isPresent();
    }

    public Long getUserIdFromAccessToken(String token) {
        return Long.parseLong(decodeAccessToken(token).get().getSubject());
    }

    public String getUserUsernameFromAccessToken(String token) {
        return decodeAccessToken(token).get().getClaim("username").asString();
    }

    public String getUserEmailFromAccessToken(String token) {
        return decodeAccessToken(token).get().getClaim("email").asString();
    }

    public String getUserStatusFromAccessToken(String token) {
        return decodeAccessToken(token).get().getClaim("status").asString();
    }

    public List<SimpleGrantedAuthority> getUserAuthoritiesFromAccessToken(String token) {
        return new JSONArray(decodeAccessToken(token).get().getClaim("authorities").asString()).toList()
            .stream().map(authority -> {
                return new SimpleGrantedAuthority(authority.toString());
            }).collect(Collectors.toList());
    }

    public List<String> getUserTenantIDsFromAccessToken(String token) {
        return new JSONArray(decodeAccessToken(token).get().getClaim("tenantIds").asString()).toList()
            .stream().map(tenantID -> {
                return tenantID.toString();
            }).collect(Collectors.toList());
    }
}