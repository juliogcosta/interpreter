package br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaCnpj;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaEmail;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaEndereco;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaTelefone;
import br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.JpaPrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.repository.PrestadorRepository;
import br.com.comigo.assistencia.mapper.aggregate.prestador.PrestadorMapper;
import br.com.comigo.common.model.records.Cnpj;
import br.com.comigo.common.model.records.Telefone;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PrestadorRepositoryImpl implements PrestadorRepository {
    private final JpaPrestadorRepository jpaPrestadorRepository;
    private final PrestadorMapper prestadorMapper;

    @Override
    public Prestador create(Prestador prestador) {
        JpaPrestador jpaPrestador = new JpaPrestador(prestador);
        
        jpaPrestador = this.jpaPrestadorRepository.save(jpaPrestador);
        prestador.setId(jpaPrestador.getId());
        return prestador;
    }
    
    @Override
    public void update(Prestador prestador, Boolean isAdmin) {
        JpaPrestador jpaPrestador = this.jpaPrestadorRepository.findById(prestador.getId())
                .orElseThrow(() -> new IllegalArgumentException("Prestador não encontrado"));
        
        if (isAdmin) {
        	jpaPrestador.setCnpj(new JpaCnpj(prestador.getCnpj().cnpj()));
        }
        jpaPrestador.setNome(prestador.getNome());
        jpaPrestador.setEmail(new JpaEmail(prestador.getEmail().email()));
        jpaPrestador.setTelefone(new JpaTelefone(prestador.getTelefone().numero(), prestador.getTelefone().tipo()));
        jpaPrestador.setWhatsapp(new JpaTelefone(prestador.getWhatsapp().numero(), prestador.getWhatsapp().tipo()));
        jpaPrestador.setEndereco(new JpaEndereco(
        		prestador.getEndereco().tipo(),
                prestador.getEndereco().logradouro(),
                prestador.getEndereco().numero(),
                prestador.getEndereco().complemento(),
                prestador.getEndereco().bairro(),
                prestador.getEndereco().cidade(),
                prestador.getEndereco().estado(),
                prestador.getEndereco().cep()));
        jpaPrestador.setStatus(prestador.getStatus());
        
        this.jpaPrestadorRepository.save(jpaPrestador);
    }

    @Override
    public Optional<Prestador> findById(Long id) {
        Optional<JpaPrestador> optional = this.jpaPrestadorRepository.findById(id);
        return optional.map(prestadorMapper::fromJpaToDomain);
    }

    @Override
    public Optional<Prestador> findByCnpj(Cnpj cnpj) {
        Optional<JpaPrestador> optional = this.jpaPrestadorRepository.findByCnpj_Cnpj(cnpj.cnpj());
        return optional.map(prestadorMapper::fromJpaToDomain);
    }

    @Override
    public List<Prestador> findByNome(String nome) {
        return this.jpaPrestadorRepository.findByNome(nome).stream()
                .map(prestadorMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prestador> findByTelefone(Telefone telefone) {
        return this.jpaPrestadorRepository.findByTelefone_Numero(telefone.numero()).stream()
                .map(prestadorMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        this.jpaPrestadorRepository.deleteById(id);
    }
}