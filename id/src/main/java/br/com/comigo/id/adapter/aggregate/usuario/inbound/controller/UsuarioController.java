package br.com.comigo.id.adapter.aggregate.usuario.inbound.controller;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.comigo.common.infrastructure.exception.RegisterNotFoundException;
import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.id.adapter.aggregate.usuario.dto.PapelDeUsuarioDTO;
import br.com.comigo.id.adapter.aggregate.usuario.dto.UsuarioDTO;
import br.com.comigo.id.application.aggregate.service.usuario.UsuarioServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioServiceImpl usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO newUsuario = this.usuarioService.create(usuarioDTO);
        return ResponseEntity.ok(newUsuario);
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> update(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        this.usuarioService.update(usuarioDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioDetailsById(@PathVariable @Nonnull Long id)
            throws RegisterNotFoundException {
        UsuarioDTO usuarioDTO = this.usuarioService.getUsuarioDetailsById(id);
        return ResponseEntity.ok(usuarioDTO);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioDTO> getUsuarioDetailsByUsername(@PathVariable @Nonnull String username)
            throws RegisterNotFoundException {
        log.info(" > username: {}", username);
        UsuarioDTO usernameDTO = this.usuarioService.getUsuarioDetailsByUsername(username);
        return ResponseEntity.ok(usernameDTO);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosByNome(@PathVariable @Nonnull String nome) {
        List<UsuarioDTO> usuarioDTOs = this.usuarioService.getFilteredUsuariosByNome(nome);
        return ResponseEntity.ok(usuarioDTOs);
    }

    @GetMapping("/telefoneNumero/{telefoneNumero}")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosByTelefone(@PathVariable @Nonnull String telefoneNumero) {
        Telefone telefone = new Telefone(telefoneNumero, null);
        List<UsuarioDTO> usuarioDTOs = this.usuarioService.getFilteredUsuariosByTelefone(telefone);
        return ResponseEntity.ok(usuarioDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable @Nonnull Long id) {
        this.usuarioService.deleteUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/papelDeUsuario")
    public ResponseEntity<Void> addPapelDeUsuarioToUsuario(@PathVariable @Nonnull Long id,
            @RequestBody PapelDeUsuarioDTO papelDeUsuarioDTO) {
        this.usuarioService.addPapelDeUsuarioToUsuario(papelDeUsuarioDTO, id);
        return ResponseEntity.ok().build();
    }
}
