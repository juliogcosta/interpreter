package br.com.comigo.id.adapter.aggregate.usuario.inbound.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yc.core.common.infrastructure.exception.RegisterNotFoundException;

import br.com.comigo.id.adapter.aggregate.usuario.dto.UsuarioForLoginDTO;
import br.com.comigo.id.application.aggregate.service.usuario.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/unsec")
public class UnsecUsuarioController {
    private final UsuarioServiceImpl usuarioService;
    
    @GetMapping("/username/{username}/to-login")
    public ResponseEntity<UsuarioForLoginDTO> getUsuario(@NonNull @PathVariable String username) throws RegisterNotFoundException {
        UsuarioForLoginDTO usuarioForLoginDTO = this.usuarioService.getUsuarioForLogin(username);
        return ResponseEntity.ok().body(usuarioForLoginDTO);
    }
}