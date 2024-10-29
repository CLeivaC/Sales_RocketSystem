package com.rocketsystem.coreapi.rocketsytem_sales_api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.Role;

import java.util.Optional;
public interface RoleRepository extends CrudRepository<Role,Integer>{

    
    Optional<Role> findByRoleName(String name);

}
