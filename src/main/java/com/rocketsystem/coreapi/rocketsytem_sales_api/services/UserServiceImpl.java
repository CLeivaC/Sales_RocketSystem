package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.UserDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.UserUpdateDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Role;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.User;
import com.rocketsystem.coreapi.rocketsytem_sales_api.exceptions.ResourceNotFoundException;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.RoleRepository;
import com.rocketsystem.coreapi.rocketsytem_sales_api.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());

        // Aplicamos el hash a la contraseña
        String passwordEncoded = passwordEncoder.encode(userDto.getHashedPassword());
        user.setHashedPassword(passwordEncoded);

        // Asignamos los roles
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByRoleName("ROLE_USER");
        optionalRoleUser.ifPresent(roles::add);

        user.setAdmin(userDto.isAdmin());
        user.setSeller(userDto.isSeller());

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByRoleName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        if (user.isSeller()) {
            Optional<Role> optionalRoleSeller = roleRepository.findByRoleName("ROLE_SELLER");
            optionalRoleSeller.ifPresent(roles::add);
        }

        user.setRoles(roles);
        user.setCreatedAt(new Date());
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public User update(Integer id, UserUpdateDto userUpdateDto) {
        // Buscar el usuario existente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Actualizar campos básicos
        if (userUpdateDto.getUsername() != null) {
            existingUser.setUsername(userUpdateDto.getUsername());
        }

        if (userUpdateDto.getHashedPassword() != null && !userUpdateDto.getHashedPassword().isEmpty()) {
            // Aplicamos el hash a la nueva contraseña
            String passwordEncoded = passwordEncoder.encode(userUpdateDto.getHashedPassword());
            existingUser.setHashedPassword(passwordEncoded);
        }

        // Actualizar estado de admin y seller
        existingUser.setAdmin(userUpdateDto.isAdmin());
        existingUser.setSeller(userUpdateDto.isSeller());

        // Manejo del estado "enabled"
        if (userUpdateDto.getEnabled() != null) {
            existingUser.setEnabled(userUpdateDto.getEnabled());
        }

        // Asignar roles basados en las propiedades admin y seller
        List<Role> roles = new ArrayList<>();
        roleRepository.findByRoleName("ROLE_USER").ifPresent(roles::add);

        if (existingUser.isAdmin()) {
            roleRepository.findByRoleName("ROLE_ADMIN").ifPresent(roles::add);
        }

        if (existingUser.isSeller()) {
            roleRepository.findByRoleName("ROLE_SELLER").ifPresent(roles::add);
        }

        existingUser.setRoles(roles);

        return userRepository.save(existingUser); // Guardar cambios
    }
}
