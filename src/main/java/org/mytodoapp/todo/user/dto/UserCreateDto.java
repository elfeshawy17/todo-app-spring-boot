package org.mytodoapp.todo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "User name is required")
    @Pattern(
            regexp = "^[A-Za-z0-9_]{3,20}$",
            message = "Username must be 3-20 characters and contain only letters, numbers, or underscores"
    )
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

}
