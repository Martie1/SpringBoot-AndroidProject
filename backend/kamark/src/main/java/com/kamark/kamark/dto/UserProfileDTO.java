package com.kamark.kamark.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO { //for update profile data

    @NotBlank(message = "{NotBlank.userProfileDTO.username}")
    @Size(min=3, max=50, message = "{Size.userProfileDTO.username}")
    private String username;

    @NotBlank(message = "{NotBlank.userProfileDTO.email}")
    @Email(message = "{Email.userProfileDTO.email}")
    private String email;

    @NotBlank(message = "{NotBlank.userProfileDTO.password}")
    @Size(min=8, max=50, message = "{Size.userProfileDTO.password}")
    private String password;

    public UserProfileDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
