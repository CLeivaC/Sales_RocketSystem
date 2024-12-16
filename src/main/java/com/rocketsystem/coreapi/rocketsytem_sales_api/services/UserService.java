package com.rocketsystem.coreapi.rocketsytem_sales_api.services;

import java.util.List;

import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.UserDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.dtos.UserUpdateDto;
import com.rocketsystem.coreapi.rocketsytem_sales_api.entities.User;

public interface UserService {

    List<User> findAll();

    User save(UserDto userDto);

    User update(Integer id, UserUpdateDto userUpdateDto);

    boolean existsByUsername(String username);
}
