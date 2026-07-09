package org.example.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationRequest {

    @NotBlank
    @Size(max = 50)
    private String recipientUsername;

    @NotBlank
    @Size(max = 1000)
    private String message;

    @NotBlank
    @Size(max = 50)
    private String sourceService;

    @NotBlank
    @Size(max = 50)
    private String eventType;
}
