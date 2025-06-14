package com.yc.pr.sec;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yc.models.sql.ua.Role;
import com.yc.pr.models.Org;
import com.yc.pr.models.RefreshToken;
import com.yc.pr.models.User;

public class JwtUtils 
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    static final String issuer = "YC-AuthService";

    private long accessTokenExpirationMs;
    private long refreshTokenExpirationMs;

    private Algorithm accessTokenAlgorithm;
    private Algorithm refreshTokenAlgorithm;
    private JWTVerifier accessTokenVerifier;
    private JWTVerifier refreshTokenVerifier;

    public JwtUtils(String accessTokenSecret, 
            int accessTokenExpirationMinutes, 
            String refreshTokenSecret, 
            int refreshTokenExpirationDays) 
    {
        accessTokenExpirationMs = (long) accessTokenExpirationMinutes * 60 * 1000;
        refreshTokenExpirationMs = (long) refreshTokenExpirationDays * 24 * 60 * 60 * 1000;
        accessTokenAlgorithm = Algorithm.HMAC512(accessTokenSecret);
        refreshTokenAlgorithm = Algorithm.HMAC512(refreshTokenSecret);
        accessTokenVerifier = JWT.require(accessTokenAlgorithm)
                .withIssuer(issuer)
                .build();
        refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
                .withIssuer(issuer)
                .build();
    }

    public JwtUtils() {
        
    }

    public String generateAccessToken(User user) 
    {
        JSONArray authorities = new JSONArray();
        user.getAuthorities().forEach(authority -> {
            authorities.put(authority.getAuthority());
        });

        JSONArray orgs = new JSONArray();
        user.getOrgs().forEach(org -> {
            JSONObject jsOrg = new JSONObject();
            jsOrg.put("name", org.getName());
            jsOrg.put("owner", org.getOwner());
            jsOrg.put("alias", org.getAlias());
            jsOrg.put("status", org.getStatus());
            orgs.put(jsOrg);
        });

        JSONArray tenantIds = new JSONArray();
        user.getTenants().forEach(tenantId -> {
            tenantIds.put(tenantId.toString());
        });

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getId().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + accessTokenExpirationMs))
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail())
                .withClaim("status", user.getStatus())
                .withClaim("authorities", authorities.toString())
                .withClaim("orgs", orgs.toString())
                .withClaim("tenantIds", tenantIds.toString())
                .sign(accessTokenAlgorithm);
    }

    public String generateRefreshToken(User user, RefreshToken refreshToken) 
    {
        JSONArray authorities = new JSONArray();
        user.getAuthorities().forEach(authority -> {
            authorities.put(authority.getAuthority());
        });

        JSONArray orgs = new JSONArray();
        user.getOrgs().forEach(org -> {
            JSONObject jsOrg = new JSONObject();
            jsOrg.put("name", org.getName());
            jsOrg.put("owner", org.getOwner());
            jsOrg.put("alias", org.getAlias());
            jsOrg.put("status", org.getStatus());
            orgs.put(jsOrg);
        });

        JSONArray tenantIds = new JSONArray();
        user.getTenants().forEach(tenantId -> {
            tenantIds.put(tenantId.toString());
        });

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getId().toString())
                .withClaim("tokenId", refreshToken.getId())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + refreshTokenExpirationMs))
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail())
                .withClaim("status", user.getStatus())
                .withClaim("authorities", authorities.toString())
                .withClaim("orgs", orgs.toString())
                .withClaim("tenantIds", tenantIds.toString())
                .sign(refreshTokenAlgorithm);
    }

    public Optional<DecodedJWT> decodeAccessToken(String token) 
    {
        try 
        {
            return Optional.of(accessTokenVerifier.verify(token));
        } 
        catch (JWTVerificationException e) 
        {
            logger.error("invalid access token", e);
        }

        return Optional.empty();
    }

    public Optional<DecodedJWT> decodeRefreshToken(String token) 
    {
        try 
        {
            return Optional.of(refreshTokenVerifier.verify(token));
        } 
        catch (JWTVerificationException e) 
        {
            logger.error("invalid refresh token", e);
        }

        return Optional.empty();
    }

    public boolean validateAccessToken(String token) 
    {
        return this.decodeAccessToken(token).isPresent();
    }

    public boolean validateRefreshToken(String token) 
    {
        return this.decodeRefreshToken(token).isPresent();
    }

    public Long getUserIdFromAccessToken(String token) 
    {
        return Long.parseLong(this.decodeAccessToken(token).get().getSubject());
    }

    public String getUserUsernameFromAccessToken(String token) 
    {
        return this.decodeAccessToken(token).get().getClaim("username").asString();
    }

    public String getUserEmailFromAccessToken(String token) 
    {
        return this.decodeAccessToken(token).get().getClaim("email").asString();
    }

    public String getUserStatusFromAccessToken(String token) 
    {
        return this.decodeAccessToken(token).get().getClaim("status").asString();
    }

    public JwtUtils setup(String accessTokenSecret)
    {
        accessTokenVerifier = JWT.require(Algorithm.HMAC512(accessTokenSecret))
                .withIssuer(issuer)
                .build();
        
        return this;
    }

    public List<SimpleGrantedAuthority> getUserAuthoritiesFromAccessToken(String token) 
    {
        return new JSONArray(this.decodeAccessToken(token).get().getClaim("authorities").asString()).toList()
            .stream().map(authority -> {
                return new SimpleGrantedAuthority(authority.toString());
            }).collect(Collectors.toList());
    }

    public List<Org> getUserOrgsFromAccessToken(String token) 
    {
        return new JSONArray(this.decodeAccessToken(token).get().getClaim("orgs").asString()).toList()
            .stream().map(authority -> {
                @SuppressWarnings("unchecked")
                Map<String, Object> jsOrg = (Map<String, Object>) authority;
                //logger.info(" > jsOrg: "+jsOrg);
                return new Org(jsOrg.get("name").toString(), 
                        jsOrg.get("owner").toString(), 
                        jsOrg.get("alias").toString(), 
                        jsOrg.get("status").toString());
            }).collect(Collectors.toList());
    }

    public List<Role> getUserRolesFromAccessToken(String token) 
    {
        return new JSONArray(this.decodeAccessToken(token).get().getClaim("roles").asString()).toList()
            .stream().map(authority -> {
                //logger.info(" > authority.toString(): "+authority.toString());
                String [] role = authority.toString().split(":");
                //logger.info(" > role.length: "+role.length);
                return new Role(role[0], role[1], role[3], role[2], false);
            }).collect(Collectors.toList());
    }

    public List<String> getUserTenantIdsFromAccessToken(String token) 
    {
        Map<String, Claim> claims = this.decodeAccessToken(token).get().getClaims();
        if (claims.containsKey("tenantIds"))
        {
            return new JSONArray(claims.get("tenantIds").asString()).toList()
                    .stream().map(tenantId -> {
                        return tenantId.toString();
                    }).collect(Collectors.toList());
        }
        else
        {
            return new ArrayList<String>();
        }
    }

    public Long getUserIdFromRefreshToken(String token) 
    {
        return Long.parseLong(this.decodeRefreshToken(token).get().getSubject());
    }

    public String getUserUsernameFromRefreshToken(String token) 
    {
        return this.decodeRefreshToken(token).get().getClaim("username").asString();
    }

    public String getUserEmailFromRefreshToken(String token) 
    {
        return this.decodeRefreshToken(token).get().getClaim("email").asString();
    }

    public String getUserStatusFromRefreshToken(String token) 
    {
        return this.decodeRefreshToken(token).get().getClaim("status").asString();
    }

    public List<SimpleGrantedAuthority> getUserAuthoritiesFromRefreshToken(String token) 
    {
        return new JSONArray(this.decodeRefreshToken(token).get().getClaim("authorities").asString()).toList()
            .stream().map(authority -> {
                return new SimpleGrantedAuthority(authority.toString());
            }).collect(Collectors.toList());
    }

    public List<Org> getUserOrgsFromRefreshToken(String token) 
    {
        return new JSONArray(this.decodeRefreshToken(token).get().getClaim("orgs").asString()).toList()
            .stream().map(authority -> {
                @SuppressWarnings("unchecked")
                Map<String, Object> jsOrg = (Map<String, Object>) authority;
                return new Org(jsOrg.get("name").toString(), 
                        jsOrg.get("owner").toString(), 
                        jsOrg.get("alias").toString(), 
                        jsOrg.get("status").toString());
            }).collect(Collectors.toList());
    }

    public List<Role> getUserRolesFromRefreshToken(String token) 
    {
        return new JSONArray(this.decodeAccessToken(token).get().getClaim("roles").asString()).toList()
                .stream().map(authority -> {
                    String [] role = authority.toString().split(":");
                    return new Role(role[0], role[1], role[3], role[2], false);
                }).collect(Collectors.toList());
    }

    public List<String> getUserTenantIdsFromRefreshToken(String token) 
    {
        return new JSONArray(this.decodeRefreshToken(token).get().getClaim("tenantIds").asString()).toList()
            .stream().map(tenantId -> {
                return tenantId.toString();
            }).collect(Collectors.toList());
    }

    public Long getTokenIdFromRefreshToken(String token) 
    {
        return this.decodeRefreshToken(token).get().getClaim("tokenId").asLong();
    }
}