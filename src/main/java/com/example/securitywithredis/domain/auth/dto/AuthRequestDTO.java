package com.example.securitywithredis.domain.auth.dto;

import com.example.securitywithredis.domain.user.validation.annotation.GenderValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class AuthRequestDTO {

    @Getter
    public static class SignUpReq{

        @NotEmpty
        @Email(message = "Invalid email format")
        private String username;

        @NotEmpty
        private String password;

        @NotEmpty
        private String nickname;

        @NotNull
        @GenderValid
        private String gender;
    }

    @Getter
    public static class LoginReq{

        @NotEmpty
        @Email(message = "Invalid email format")
        private String username;

        @NotEmpty
        private String password;
    }
}
