package com.example.alerting;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertRequest {
    private String alertMessage;
    private String severity;
    private String email;
    private String phoneNumber;
}
