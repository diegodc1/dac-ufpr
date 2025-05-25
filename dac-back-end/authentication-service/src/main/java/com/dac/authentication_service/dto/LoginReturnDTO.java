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
    private String access_token;
    private String token_type;
    private String name;
    private String login;
    private String tipo;
}
