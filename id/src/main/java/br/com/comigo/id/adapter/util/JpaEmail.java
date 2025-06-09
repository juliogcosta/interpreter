package br.com.comigo.id.adapter.util;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class JpaEmail {

    @Column(nullable = true, unique = false)
    private String email;

    public JpaEmail(String email) {
        this.setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        if (!value.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.email = value;
    }
}