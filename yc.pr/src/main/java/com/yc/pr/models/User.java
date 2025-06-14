package com.yc.pr.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class User implements UserDetails
{
    /**
     * 
     */
    private static final long serialVersionUID = 7874348088593275706L;

    final static public String ROLE_PREFIX = "ROLE_";

    static public enum EStatus 
    {
        SUSPENDED, ACTIVE, CANCELED
    }

    private Long id = -1L;

    @NotBlank
    @Size(max = 20)
    private String username = null;

    @NotBlank
    @Size(max = 120)
    private String password = null;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email = null;

    private String status = null;

    private Set<Role> roles = new HashSet<>();

    private Set<Org> orgs = new HashSet<>();

    private Set<String> tenants = new HashSet<>();

    public User()
    {

    }

    public User(String username, 
            String password, 
            String email)
    {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, 
            String password, 
            String email, 
            String status,
            Collection<? extends GrantedAuthority> authorities)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.roles = authorities.stream().map(authority ->
        {
            Role role = new Role();
            role.setName(authority.toString());
            role.setStatus(com.yc.models.sql.ua.Role.EStatus.ACTIVE.name());
            return role;
        }).collect(Collectors.toSet());
    }

    public User(String username, 
            String password, 
            String email, 
            String status,
            Collection<? extends GrantedAuthority> authorities,
            Set<Org> orgs)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.roles = authorities.stream().map(authority ->
        {
            Role role = new Role();
            role.setName(authority.toString());
            role.setStatus(com.yc.models.sql.ua.Role.EStatus.ACTIVE.name());
            return role;
        }).collect(Collectors.toSet());
        this.orgs = orgs;
    }

    public User(String username, 
            String password, 
            String email, 
            String status,
            Set<Role> roles)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.roles = roles;
    }

    public User(String username, 
            String password, 
            String email, 
            String status,
            Set<Role> roles,
            Set<Org> orgs)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.roles = roles;
        this.orgs = orgs;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public Set<Org> getOrgs()
    {
        return orgs;
    }

    public void setOrgs(Set<Org> orgs)
    {
        this.orgs = orgs;
    }

    public Set<String> getTenants()
    {
        return tenants;
    }

    public void setTenants(Set<String> tenants)
    {
        this.tenants = tenants;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return roles.stream().filter(role -> role.getStatus().equals(com.yc.models.sql.ua.Role.EStatus.ACTIVE.name())).map(role -> {
            if (role.getName().startsWith(ROLE_PREFIX)) {
                return new SimpleGrantedAuthority(role.getName());
            } else {
                return new SimpleGrantedAuthority(ROLE_PREFIX.concat(role.getName()));
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        if (status == null) 
        {
            return false;
        }
        else
        {
            return status.equals(EStatus.ACTIVE.name());
        }
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (username == null)
        {
            if (other.username != null)
                return false;
        }
        else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", status="
                + status + ", roles=" + roles + ", orgs=" + orgs + ", tenants=" + tenants + "]";
    }

}
