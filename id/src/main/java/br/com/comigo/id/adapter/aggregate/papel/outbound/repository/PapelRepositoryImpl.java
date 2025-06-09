package br.com.comigo.id.adapter.aggregate.papel.outbound.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.com.comigo.id.adapter.aggregate.papel.outbound.JpaPapel;
import br.com.comigo.id.domain.aggregate.papel.Papel;
import br.com.comigo.id.domain.aggregate.papel.repository.PapelRepository;
import br.com.comigo.id.mapper.aggregate.papel.PapelMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PapelRepositoryImpl implements PapelRepository {
    private final JpaPapelRepository jpaPapelRepository;
    private final PapelMapper papelMapper;

    @Override
    public Papel create(Papel papel) {
        JpaPapel jpaPapel = new JpaPapel(papel);
        jpaPapel = this.jpaPapelRepository.save(jpaPapel);
        papel.setId(jpaPapel.getId());
        return papel;
    }

    @Override
    public void update(Papel papel) {
        JpaPapel jpaPapel = this.jpaPapelRepository.findById(papel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Papel não encontrado"));
        jpaPapel.setStatus(papel.getStatus());
        this.jpaPapelRepository.save(jpaPapel);
    }

    @Override
    public Optional<Papel> findById(Long id) {
        Optional<JpaPapel> optional = this.jpaPapelRepository.findById(id);
        return optional.map(papelMapper::fromJpaToDomain);
    }

    @Override
    public Optional<Papel> findByNome(String nome) {
        Optional<JpaPapel> optional = this.jpaPapelRepository.findByNome(nome);
        return optional.map(papelMapper::fromJpaToDomain);
    }

    @Override
    public List<Papel> findAll() {
        return this.jpaPapelRepository.findAll().stream()
                .map(papelMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        this.jpaPapelRepository.deleteById(id);
    }
}