package br.com.comigo.assistencia.domain.projection;

public interface PrestadorSetupDeItemDoServicoProjection {
  Long getPrestadorId();

  String getPrestadorNome();

  String getPrestadorCnpj();

  String getPrestadorTelefoneNumero();

  String getPrestadorTelefoneTipo();

  String getPrestadorEmail();

  String getPrestadorEnderecoRua();

  String getPrestadorEnderecoNumero();

  String getPrestadorEnderecoComplemento();

  String getPrestadorEnderecoBairro();

  String getPrestadorEnderecoCidade();

  String getPrestadorEnderecoEstado();

  String getPrestadorEnderecoCep();

  String getSetupDeItemDoServicoStatus();

  Integer getSetupDeItemDoServicoPrecoUnitario();

  Long getSetupDeItemDoServicoItemDeServicoId();
}