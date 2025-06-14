package com.yc.pr.sec;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.yc.pr.models.Org;
import com.yc.pr.models.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccessTokenFilter extends OncePerRequestFilter
{
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        try
        {
            //System.out.println(" > "+getClass().getCanonicalName()+": Authorization: "+request.getHeader("Authorization"));

            Optional<String> accessToken = parseAccessToken(request);
            //System.out.println(" > "+getClass().getCanonicalName()+": accessToken: "+accessToken.get());

            if (accessToken != null && accessToken.isPresent() && this.jwtUtils.validateAccessToken(accessToken.get()))
            {
                String username = this.jwtUtils.getUserUsernameFromAccessToken(accessToken.get());
                String status = this.jwtUtils.getUserStatusFromAccessToken(accessToken.get());
                List<SimpleGrantedAuthority> authorities = this.jwtUtils.getUserAuthoritiesFromAccessToken(accessToken.get());
                List<Org> orgs = this.jwtUtils.getUserOrgsFromAccessToken(accessToken.get());
                User user = new User(username, null, null, status, authorities, new HashSet<Org>(orgs));
                user.setTenants(new HashSet<String>(this.jwtUtils.getUserTenantIdsFromAccessToken(accessToken.get())));
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        }
        catch (Exception e)
        {
            this.logger.error("cannot set authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> parseAccessToken(HttpServletRequest request)
    {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer "))
        {
            return Optional.of(authHeader.replace("Bearer ", ""));
        }

        return Optional.empty();
    }
}
