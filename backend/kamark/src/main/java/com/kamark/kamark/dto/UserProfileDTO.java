package com.kamark.kamark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String username;
    private String email;
    private String password;

    public UserProfileDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
