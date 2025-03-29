package com.dac.authentication_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginReturnDTO {
    private String userId;
    private String token;
    private String name;
    private String login;
    private String role;
}
