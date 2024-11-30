package com.kamark.kamark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomDTO {
    @NotBlank(message = "{NotBlank.generic}")
    @Size(min = 1, max = 18, message = "{Size.generic}")
    private String name;
}
