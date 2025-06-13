package br.com.comigo.assistencia.adapter.aggregate.prestador.inbound.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yc.core.common.infrastructure.exception.BusinessRuleConsistencyException;
import com.yc.core.common.infrastructure.exception.ControlledException;
import com.yc.core.common.infrastructure.exception.IncompleteRegisterException;
import com.yc.core.common.model.records.Cnpj;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.PrestadorDTO;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.PrestadorResponse;
import br.com.comigo.assistencia.application.aggregate.service.prestador.PrestadorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/prestador")
public class PrestadorController {
    private final PrestadorServiceImpl prestadorService;

    @PostMapping
    public ResponseEntity<PrestadorDTO> create(@Valid @RequestBody PrestadorDTO prestadorDTO) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException {
    	PrestadorDTO prestador = this.prestadorService.create(prestadorDTO);
        return ResponseEntity.ok(prestador);
    }

    @PutMapping
    public ResponseEntity<PrestadorDTO> update(@Valid @RequestBody PrestadorDTO prestadorDTO) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException {
        this.prestadorService.update(prestadorDTO, false);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin")
    public ResponseEntity<PrestadorDTO> updateByAdmin(@Valid @RequestBody PrestadorDTO prestadorDTO) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException {
        this.prestadorService.update(prestadorDTO, true);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestadorResponse> getPrestadorDetailsById(@PathVariable(required = true) Long id) throws ControlledException {
    	PrestadorResponse prestadorResponse = this.prestadorService.getPrestadorDetailsById(id);
        return ResponseEntity.ok(prestadorResponse);
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<PrestadorResponse> getPrestadorDetailsByCnpj(@PathVariable(required = true) String cnpj) throws ControlledException {
        PrestadorResponse prestadorResponse = this.prestadorService.getPrestadorDetailsByCnpj(new Cnpj(cnpj));
        return ResponseEntity.ok(prestadorResponse);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<PrestadorDTO>> getFilteredPrestadorsByNome(@PathVariable(required = true) String nome) {
        List<PrestadorDTO> prestadorDTOs = this.prestadorService.getFilteredPrestadorsByNome(nome);
        return ResponseEntity.ok(prestadorDTOs);
    }

    @GetMapping("/telefoneNumero/{telefoneNumero}")
    public ResponseEntity<List<PrestadorDTO>> getFilteredPrestadorsByTelefone(@PathVariable(required = true) String telefoneNumero) {
        Telefone telefone = new Telefone(telefoneNumero, null);
        List<PrestadorDTO> prestadorDTOs = this.prestadorService.getFilteredPrestadorsByTelefone(telefone);
        return ResponseEntity.ok(prestadorDTOs);
    }
}
