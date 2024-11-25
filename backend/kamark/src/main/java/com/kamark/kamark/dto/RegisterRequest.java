package com.kamark.kamark.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "{NotBlank.generic}")
    @Size(min = 3, max = 50, message = "{Size.registerRequest.username}")
    private String username;

    @NotBlank(message = "{NotBlank.generic}")
    @Email(message = "{Email.generic}")
    private String email;

    @NotBlank(message = "{NotBlank.generic}")
    @Size(min = 8, max = 50, message = "{Size.registerRequest.password}")
    private String password;
}