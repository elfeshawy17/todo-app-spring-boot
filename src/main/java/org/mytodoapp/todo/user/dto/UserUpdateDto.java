package org.mytodoapp.todo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.mytodoapp.todo.user.entity.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    @NotNull(message = "User id can not be null")
    private Long id;

    @NotBlank(message = "User name is required")
    @Pattern(
            regexp = "^[A-Za-z0-9_]{3,20}$",
            message = "Username must be 3-20 characters and contain only letters, numbers, or underscores"
    )
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    private Role role;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

}
