package com.rocketsystem.coreapi.rocketsytem_sales_api.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.RoleDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.UserDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.User;
import com.rocketsystem.coreapi.rocketsytem_sales_api.services.UserService;

import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rocketsystem/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDto> list() {
        return userService.findAll().stream()
                .map(user -> {
                    // Convierte la lista de Role a RoleDto
                    List<RoleDto> roleDtos = user.getRoles().stream()
                            .map(role -> new RoleDto(role.getRoleId(), role.getRoleName()))
                            .collect(Collectors.toList());

                    // Crea y devuelve un UserDto con la lista de RoleDto
                    return new UserDto(
                            user.getUserId(),
                            user.getUsername(),
                            user.getCreatedAt(),
                            user.getToken(),
                            roleDtos,
                            user.isEnabled());
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        userDto.setAdmin(false); // Establece admin a false si es necesario
        User savedUser = userService.save(userDto); // Guarda el usuario

        // Convierte la lista de Role a RoleDto
        List<RoleDto> roleDtos = savedUser.getRoles().stream()
                .map(role -> new RoleDto(role.getRoleId(), role.getRoleName()))
                .collect(Collectors.toList());

        // Crear un UserDto desde el objeto User guardado
        UserDto createdUserDto = new UserDto(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getCreatedAt(),
                savedUser.getToken(),
                roleDtos,
                savedUser.isEnabled());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto); // Retorna el UserDto creado
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
