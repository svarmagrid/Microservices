package org.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationRequest {

    private String recipientUsername;
    private String message;
    private String sourceService;
    private String eventType;
}
