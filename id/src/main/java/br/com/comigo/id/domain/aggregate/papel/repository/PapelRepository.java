package br.com.comigo.id.domain.aggregate.papel.repository;

import java.util.List;
import java.util.Optional;

import br.com.comigo.id.domain.aggregate.papel.Papel;

public interface PapelRepository {
    public Papel create(Papel papel);

    public void update(Papel papel);

    public Optional<Papel> findById(Long id);

    public Optional<Papel> findByNome(String nome);

    public List<Papel> findAll();

    public void deleteById(Long id);
}