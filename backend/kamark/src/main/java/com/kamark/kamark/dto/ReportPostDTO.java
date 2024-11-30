package com.kamark.kamark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kamark.kamark.entity.ReportStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportPostDTO {
    @NotBlank
    @Size(min=10,max=50, message = "Reason has to have have <3,50> characters")
    private String reason;
}