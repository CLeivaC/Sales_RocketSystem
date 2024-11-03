package com.rocketsystem.coreapi.rocketsytem_sales_api.security.filter;

import static com.rocketsystem.coreapi.rocketsytem_sales_api.security.TokenJwtConfig.CONTENT_TYPE;
import static com.rocketsystem.coreapi.rocketsytem_sales_api.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.rocketsystem.coreapi.rocketsytem_sales_api.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.rocketsystem.coreapi.rocketsytem_sales_api.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocketsystem.coreapi.rocketsytem_sales_api.security.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Filtro para validar el token
public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(HEADER_AUTHORIZATION);

        // Permitir solicitudes al endpoint de refresh sin autenticación
        if (request.getRequestURI().equals("/rocketsystem/auth/refresh") && request.getMethod().equals("POST")) {
            chain.doFilter(request, response); // No hacemos nada, simplemente permitimos el acceso
            return;
        }

        // Si no hay encabezado o el formato del token es incorrecto, se permite el
        // acceso
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");
        try {
            // Validar el token y obtener los claims
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            String username = claims.getSubject();
            Object authoritiesClaims = claims.get("authorities");

            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                    new ObjectMapper()
                            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                            .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            // Crear el token de autenticación
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                    null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response); // Continúa con la siguiente cadena de filtros

        } catch (JwtException e) {
            // Manejo de excepciones para el token
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token JWT no es válido");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
            return; // Asegúrate de que no se llame a la cadena de filtros después de un error
        }
    }

}
