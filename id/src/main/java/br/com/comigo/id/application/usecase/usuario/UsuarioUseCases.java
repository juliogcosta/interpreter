package br.com.comigo.id.application.usecase.usuario;

import java.util.List;

import com.yc.core.common.infrastructure.exception.RegisterNotFoundException;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.id.adapter.aggregate.usuario.dto.PapelDeUsuarioDTO;
import br.com.comigo.id.adapter.aggregate.usuario.dto.UsuarioDTO;
import br.com.comigo.id.adapter.aggregate.usuario.dto.UsuarioForLoginDTO;

public interface UsuarioUseCases {
    public UsuarioDTO create(UsuarioDTO dto);

    public void update(UsuarioDTO dto);

    public UsuarioDTO getUsuarioDetailsById(Long id) throws RegisterNotFoundException;

    public UsuarioDTO getUsuarioDetailsByUsername(String username) throws RegisterNotFoundException;
    
    public List<UsuarioDTO> getFilteredUsuariosByNome(String nome);
    
    public List<UsuarioDTO> getFilteredUsuariosByTelefone(Telefone telefone);

    public void deleteUsuario(Long id);

    public void addPapelDeUsuarioToUsuario(PapelDeUsuarioDTO veiculoDTO, Long clienteId);

    public void deletePapelDeUsuario(Long id);

    public UsuarioForLoginDTO getUsuarioForLogin(String username);
}
