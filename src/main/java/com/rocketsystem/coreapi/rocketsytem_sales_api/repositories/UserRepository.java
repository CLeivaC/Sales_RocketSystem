package com.rocketsystem.coreapi.rocketsytem_sales_api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.User;

public interface UserRepository extends CrudRepository<User,Integer> {
    boolean existsByUsername(String username);

}
