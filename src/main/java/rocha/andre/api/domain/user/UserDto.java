package rocha.andre.api.domain.user;

import jakarta.validation.constraints.NotNull;

public record UserDTO(
        @NotNull
        String login,
        @NotNull
        String password
) {  }
