package br.com.comigo.id.domain.projection;

import br.com.comigo.id.adapter.util.JpaEmail;
import br.com.comigo.id.adapter.util.JpaTelefone;

public interface UsuarioAndPapelProjection {
    Long getId();
    String getUsername();
    String getPassword();
    String getNome();
    JpaTelefone getTelefone();
    JpaEmail getEmail();
    String getStatus();
    String getPapelNome();
    String getPapelStatus();
}