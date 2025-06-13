package br.com.comigo.autenticador.application.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yc.core.common.infrastructure.exception.RegisterNotFoundException;
import com.yc.core.common.model.records.Email;

import br.com.comigo.autenticador.adaper.outbound.restclient.UnsecUsuarioRestClient;
import br.com.comigo.autenticador.domain.Role;
import br.com.comigo.autenticador.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UnsecUsuarioRestClient unsecUsuarioRestClient;

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            ResponseEntity<String> responseEntity = unsecUsuarioRestClient.getUsuario(username);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String raw = responseEntity.getBody();
                JSONObject jsUsuario = new JSONObject(raw);
                log.info(" > jsUsuario: {}", jsUsuario);
                User.Status status = User.Status.ACTIVE;
                if (jsUsuario.has("status")) {
                    status = jsUsuario.getString("status").equals("ATIVO") ? User.Status.ACTIVE : User.Status.INACTIVE;
                } else {
                    status = User.Status.INACTIVE;
                }
                Email email = null;
                if (jsUsuario.has("email")) {
                    email = new Email(jsUsuario.getString("email"));
                } else {
                    throw new RegisterNotFoundException("Email not registered for user: " + username);
                }
                
                final List<Role> roles = new ArrayList<>(); 
                jsUsuario.getJSONArray("papeis").forEach(item -> {
                    JSONObject jsRole = (JSONObject) item;
                    Role role = new Role(jsRole.getString("nome"), 
                        jsRole.getString("status").equals("ATIVO") ? Role.Status.ACTIVE : Role.Status.INACTIVE);
                    roles.add(role);
                });

                return new User(
                    jsUsuario.getString("username"), 
                    jsUsuario.getString("password"),
                    jsUsuario.getString("nome"), 
                    email,
                    status,
                    roles);
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new UsernameNotFoundException(e.getMessage(), e.getCause());
        }
    }
}