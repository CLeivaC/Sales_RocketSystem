package com.rocketsystem.coreapi.rocketsytem_sales_api.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.User;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.rocketsystem.coreapi.rocketsytem_sales_api.security.TokenJwtConfig.*;


//Creación de token al hacer login
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository; // Aquí se inyecta el repositorio
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        User user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getHashedPassword();
        } catch (StreamReadException e) {

            e.printStackTrace();
        } catch (DatabindException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
    
        Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(roles)).build();
    
        // Generar access token
        String accessToken = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();
    
        // Generar refresh token
        String refreshToken = Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + 604800000)) // 7 días
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();
    

         // Guardar el refresh token en la base de datos
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();
            currentUser.setToken(refreshToken); // Asegúrate de tener el método setRefreshToken en tu entidad User
            userRepository.save(currentUser); // Guardar el usuario con el nuevo refresh token
        }

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + accessToken);
    
        Map<String, String> body = new HashMap<>();
        body.put("accessToken", accessToken);
        body.put("refreshToken", refreshToken);
        body.put("username", username);
        body.put("message", String.format("Hola %s, has iniciado sesión con éxito!", username));
    
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);

       

       
    }
    

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        Map<String, String> body = new HashMap<>();
        body.put("message", String.format("Error en la autenticacion username o password incorrectos!"));
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);

    }

}
