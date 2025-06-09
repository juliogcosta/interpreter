package br.com.comigo.id.domain.aggregate.usuario;

public class PapelDeUsuario {
  private Long id;
  private Long papelId;
  private Usuario usuario;

  public PapelDeUsuario(Long id, Long papelId) {
    this.id = id;
    this.papelId = papelId;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPapelId() {
    return this.papelId;
  }

  public void setPapelId(Long papelId) {
    this.papelId = papelId;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario cliente) {
    this.usuario = cliente;
  }
}