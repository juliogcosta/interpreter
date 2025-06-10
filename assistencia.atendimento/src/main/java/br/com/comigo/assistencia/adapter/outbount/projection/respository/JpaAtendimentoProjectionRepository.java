package br.com.comigo.assistencia.adapter.outbount.projection.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.comigo.assistencia.adapter.outbount.projection.JpaAtendimentoProjection;

import java.util.List;
import java.util.UUID;

import br.com.comigo.assistencia.adapter.outbount.util.JpaTelefone;

public interface JpaAtendimentoProjectionRepository extends JpaRepository<JpaAtendimentoProjection, UUID> {
	List<JpaAtendimentoProjection> findByStatus(String status);

	List<JpaAtendimentoProjection> findByClienteDocFiscal(String clienteDocFiscal);

	List<JpaAtendimentoProjection> findByClienteTelefone(JpaTelefone clienteTelefone);

	List<JpaAtendimentoProjection> findByVeiculoPlaca(String veiculoPlaca);

	List<JpaAtendimentoProjection> findByTipoOcorrencia(String tipoOcorrencia);
}
