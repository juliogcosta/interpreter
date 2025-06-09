package br.com.comigo.assistencia.domain.projection;

public interface ServicoItemDeServicoProjection {
  String getServicoId();

  String getServicoNome();

  String getServicoDescricao();

  String getServicoStatus();

  Long getItemDeServicoId();

  String getItemDeServicoNome();

  String getItemDeServicoDescricao();

  String getItemDeServicoUnidadeMedida();
}