package br.com.comigo.assistencia.adapter.outbount.projection.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.comigo.assistencia.adapter.outbount.projection.JpaServicoProjection;

import java.util.List;
import java.util.UUID;

public interface JpaServicoProjectionRepository extends JpaRepository<JpaServicoProjection, UUID> {

    public List<JpaServicoProjection> findByNomeContainingIgnoreCase(String nome);

    public List<JpaServicoProjection> findByCertificacaoIso(Boolean certificacaoIso);
}
