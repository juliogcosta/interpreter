package br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.yc.core.common.model.records.Cpf;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.JpaCliente;
import br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.JpaVeiculo;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaCpf;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaEmail;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaEndereco;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaTelefone;
import br.com.comigo.assistencia.domain.aggregate.cliente.Cliente;
import br.com.comigo.assistencia.domain.aggregate.cliente.Veiculo;
import br.com.comigo.assistencia.domain.aggregate.cliente.repository.ClienteRepository;
import br.com.comigo.assistencia.mapper.aggregate.cliente.ClienteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ClienteRepositoryImpl implements ClienteRepository {
    private final JpaClienteRepository jpaClienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public Cliente create(Cliente cliente) {
        JpaCliente jpaCliente = new JpaCliente(cliente);
        jpaCliente = this.jpaClienteRepository.save(jpaCliente);
        cliente.setId(jpaCliente.getId());
        return cliente;
    }

    @Override
    public void update(Cliente cliente) {
        JpaCliente jpaCliente = this.jpaClienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        jpaCliente.setCpf(new JpaCpf(cliente.getCpf().cpf()));
        jpaCliente.setNome(cliente.getNome());
        jpaCliente.setEmail(new JpaEmail(cliente.getEmail().email()));
        jpaCliente.setTelefone(new JpaTelefone(cliente.getTelefone().numero(), cliente.getTelefone().tipo()));
        jpaCliente.setWhatsapp(new JpaTelefone(cliente.getWhatsapp().numero(), cliente.getWhatsapp().tipo()));
        jpaCliente.setEndereco(new JpaEndereco(cliente.getEndereco().tipo(), cliente.getEndereco().logradouro(), 
        		cliente.getEndereco().numero(), cliente.getEndereco().complemento(), cliente.getEndereco().bairro(),
                cliente.getEndereco().cidade(), cliente.getEndereco().estado(), cliente.getEndereco().cep()));
        jpaCliente.setDataNascimento(cliente.getDataNascimento());
        
        if (cliente.getVeiculos() == null) {
            jpaCliente.getVeiculos().clear();
        } else {
            Map<String, Veiculo> novosVeiculosMap = cliente.getVeiculos().stream()
                    .collect(Collectors.toMap(Veiculo::getPlaca, Function.identity()));
            
            Iterator<JpaVeiculo> iterator = jpaCliente.getVeiculos().iterator();
            while (iterator.hasNext()) {
                JpaVeiculo jpaVeiculo = iterator.next();
                Veiculo veiculo = novosVeiculosMap.get(jpaVeiculo.getPlaca());
                
                if (veiculo == null) {
                    iterator.remove();
                } else {
                    jpaVeiculo.setMarca(veiculo.getMarca());
                    jpaVeiculo.setModelo(veiculo.getModelo());
                    jpaVeiculo.setValidado(veiculo.getValidado());
                    novosVeiculosMap.remove(veiculo.getPlaca());
                }
            }
            
            novosVeiculosMap.values().forEach(newItem -> {
                JpaVeiculo jpaVeiculo = new JpaVeiculo();
                jpaVeiculo.setMarca(newItem.getMarca());
                jpaVeiculo.setModelo(newItem.getModelo());
                jpaVeiculo.setPlaca(newItem.getPlaca());
                jpaCliente.getVeiculos().add(jpaVeiculo);
            });
        }
        
        this.jpaClienteRepository.save(jpaCliente);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        Optional<JpaCliente> optional = this.jpaClienteRepository.findById(id);
        return optional.map(clienteMapper::fromJpaToDomain);
    }

    @Override
    public Optional<Cliente> findByCpf(Cpf cpf) {
        Optional<JpaCliente> optional = this.jpaClienteRepository.findByCpf_Cpf(cpf.cpf());
        return optional.map(clienteMapper::fromJpaToDomain);
    }

    @Override
    public List<Cliente> findByNome(String nome) {
        return this.jpaClienteRepository.findByNome(nome).stream()
                .map(clienteMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> findByTelefone(Telefone telefone) {
        return this.jpaClienteRepository.findByTelefone_Numero(telefone.numero()).stream()
                .map(clienteMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        this.jpaClienteRepository.deleteById(id);
    }
}