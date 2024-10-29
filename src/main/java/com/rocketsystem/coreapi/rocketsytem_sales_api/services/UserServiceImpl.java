package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.UserDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Role;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.User;
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
}
