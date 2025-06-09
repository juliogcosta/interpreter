package br.com.comigo.assistencia.domain.projection;

import java.util.Date;

public interface ClienteVeiculoProjection {
  Long getClienteId();

  String getClienteNome();

  String getClienteCpf();

  String getClienteTelefoneNumero();

  String getClienteTelefoneTipo();

  String getClienteWhatsappNumero();

  String getClienteWhatsappTipo();

  String getClienteEndereco();

  Date getClienteDataNascimento();

  Long getVeiculoId();

  String getVeiculoPlaca();

  String getVeiculoModelo();

  String getVeiculoMarca();

  String getVeiculoAno();

  String getVeiculoCor();
}