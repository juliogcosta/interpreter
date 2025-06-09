package br.com.comigo.id.application.usecase.papel;

import java.util.List;

import br.com.comigo.id.adapter.aggregate.papel.dto.PapelDTO;

public interface PapelUseCases {
    public PapelDTO create(PapelDTO dto);

    public void update(PapelDTO dto);

    public PapelDTO getPapelDetailsById(Long id);

    public PapelDTO getPapelDetailsByNome(String nome);

    public List<PapelDTO> getAll();

    public void deletePapel(Long id);
}