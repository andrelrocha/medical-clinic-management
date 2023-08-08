package rocha.andre.api.doctor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import rocha.andre.api.address.DataAddress;

public record DataDoctor(
        @NotBlank
        String name,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String crm,
        @NotNull //não é string, mas enum, logo não é notBlank
        Specialty specialty,
        @NotNull
        @Valid
        DataAddress address
) { }
