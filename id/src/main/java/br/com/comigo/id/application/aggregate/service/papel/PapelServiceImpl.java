package br.com.comigo.id.application.aggregate.service.papel;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.comigo.id.adapter.aggregate.papel.dto.PapelDTO;
import br.com.comigo.id.application.usecase.papel.PapelUseCases;
import br.com.comigo.id.domain.aggregate.papel.Papel;
import br.com.comigo.id.domain.aggregate.papel.repository.PapelRepository;
import br.com.comigo.id.mapper.aggregate.papel.PapelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PapelServiceImpl implements PapelUseCases {

    private final PapelRepository papelRepository;
    private final PapelMapper papelMapper;
    
    @Override
    public PapelDTO create(PapelDTO dto) {
        Papel papel = this.papelRepository.create(this.papelMapper.toDomain(dto));
        return this.papelMapper.toDto(papel);
    }

    @Override
    public void update(PapelDTO dto) {
        this.papelRepository.update(this.papelMapper.toDomain(dto));
    }

    @Override
    public PapelDTO getPapelDetailsById(Long id) {
        Papel papel = this.papelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Papel not found"));
        return this.papelMapper.toDto(papel);
    }

    @Override
    public PapelDTO getPapelDetailsByNome(String nome) {
        Papel papel = this.papelRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Papel not found"));
        return this.papelMapper.toDto(papel);
    }

    @Override
    public List<PapelDTO> getAll() {
        return this.papelRepository.findAll().stream()
                .map(this.papelMapper::toDto)
                .toList();
    }

    @Override
    public void deletePapel(Long id) {
        this.papelRepository.deleteById(id);
    }
}