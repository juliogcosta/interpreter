package br.com.comigo.assistencia.adapter.aggregate.outbound.util;

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
        this.email = value;
    }
}