package org.mytodoapp.todo.user.util;

import org.mytodoapp.todo.user.entity.Role;
import org.mytodoapp.todo.user.entity.User;

public class UserTestDataFactory {

    public static User createUser(Long id, String username, String email, String password, Role role) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }

}
