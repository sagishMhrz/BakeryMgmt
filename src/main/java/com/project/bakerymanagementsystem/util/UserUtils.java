package com.project.bakerymanagementsystem.util;

import com.project.bakerymanagementsystem.dto.UserDTO;
import com.project.bakerymanagementsystem.service.UserService;
import com.project.bakerymanagementsystem.entity.Employee;

public class UserUtils {
    private UserUtils() {}

    public static UserDTO getUserData(UserService userService, String email) {
        Employee user = userService.getUserByEmail(email);

        UserDTO userData = userService.getUserFromEntity(user);

        return userData;
    }
}
