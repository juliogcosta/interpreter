package br.com.comigo.assistencia.adapter.aggregate.cliente.inbound.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.comigo.assistencia.adapter.aggregate.cliente.dto.ClienteDTO;
import br.com.comigo.assistencia.application.aggregate.service.cliente.ClienteServiceImpl;
import br.com.comigo.common.infrastructure.exception.ControlledException;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.common.model.records.Cpf;
import br.com.comigo.common.model.records.Telefone;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ClienteController {
    private final ClienteServiceImpl clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO clienteDTO) throws IncompleteRegisterException, ControlledException {
        ClienteDTO newCliente = this.clienteService.create(clienteDTO);
        return ResponseEntity.ok(newCliente);
    }

    @PutMapping
    public ResponseEntity<ClienteDTO> update(@Valid @RequestBody ClienteDTO clienteDTO) throws IncompleteRegisterException, ControlledException {
        this.clienteService.update(clienteDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteDetailsById(@PathVariable Long id) {
        ClienteDTO clienteDTO = this.clienteService.getClienteDetailsById(id);
        return ResponseEntity.ok(clienteDTO);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDTO> getClienteDetailsByCpf(@PathVariable(name = "cpf") String cpfValue) {
        Cpf cpf = new Cpf(cpfValue);
        ClienteDTO clienteDTO = this.clienteService.getClienteDetailsByCpf(cpf);
        return ResponseEntity.ok(clienteDTO);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ClienteDTO>> getClientesByNome(@PathVariable String nome) {
        List<ClienteDTO> clienteDTOs = this.clienteService.getFilteredClientesByNome(nome);
        return ResponseEntity.ok(clienteDTOs);
    }

    @GetMapping("/telefoneNumero/{telefoneNumero}")
    public ResponseEntity<List<ClienteDTO>> getClientesByTelefone(@PathVariable String telefoneNumero) {
        Telefone telefone = new Telefone(telefoneNumero, null);
        List<ClienteDTO> clienteDTOs = this.clienteService.getFilteredClientesByTelefone(telefone);
        return ResponseEntity.ok(clienteDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletCliente(@PathVariable Long id) {
        this.clienteService.deleteCliente(id);
        return ResponseEntity.ok().build();
    }
}
