package org.mytodoapp.todo.security.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RefreshTokenRequestDto {
    private String refreshToken;
}
