package com.rocketsystem.coreapi.rocketsytem_sales_api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Role;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.User;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static com.rocketsystem.coreapi.rocketsytem_sales_api.security.TokenJwtConfig.SECRET_KEY;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import java.util.Optional;

import java.util.List;

import java.util.Collection;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/rocketsystem/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository; // Inyectar el UserRepository

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || !validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is invalid.");
        }

        String username = extractUsernameFromToken(refreshToken);
        
        // Obtener el usuario de la base de datos
        // Obtener el usuario de la base de datos
       // Obtener el usuario de la base de datos
       Optional<User> optionalUser = userRepository.findByUsername(username);
       if (!optionalUser.isPresent()) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
       }

       User user = optionalUser.get(); // Obtener el usuario
       List<GrantedAuthority> roles = convertToGrantedAuthorities(user.getRoles()); // Convertir roles a GrantedAuthority
        // Generar un nuevo access token con roles
        String newAccessToken;
        try {
            newAccessToken = generateAccessToken(username, roles);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating token.");
        }

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", refreshToken); // Puedes decidir si deseas generar un nuevo refresh token

        return ResponseEntity.ok(response);
    }

    private boolean validateToken(String token) {
        // Lógica para validar el refresh token
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token); // Corrige esto
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String extractUsernameFromToken(String token) {
        Claims claims =  Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload(); // Corrige esto
        return claims.getSubject();
    }

    private String generateAccessToken(String username, Collection<? extends GrantedAuthority> roles) throws JsonProcessingException {
        Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(roles)).build(); // Asegúrate de usar 'put' en lugar de 'add'

        return Jwts.builder()
                .subject(username)
                .claims(claims) // Pasar los claims al builder
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(SECRET_KEY) // Firmar el token
                .compact(); // Generar el token
    }

     private List<GrantedAuthority> convertToGrantedAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName())) // Asegúrate de que `getName()` devuelve el nombre del rol
                .collect(Collectors.toList());
    }
}
