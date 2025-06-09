package br.com.comigo.autenticador.adaper.dto;

import java.util.List;

public class AuthrorizationResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private String name;
    private String email;
    private List<String> roles;

    public AuthrorizationResponse(String accessToken, 
            String username, 
            String name, 
            String email, 
            List<String> roles) {
        this.token = accessToken;
        this.username = username;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}