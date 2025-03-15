package com.example.securitywithredis.application.auth;

import com.example.securitywithredis.domain.auth.converter.AuthConverter;
import com.example.securitywithredis.domain.auth.dto.AuthRequestDTO;
import com.example.securitywithredis.domain.auth.dto.AuthResponseDTO;
import com.example.securitywithredis.domain.auth.service.JoinService;
import com.example.securitywithredis.domain.auth.service.ReissueService;
import com.example.securitywithredis.domain.user.UserEntity;
import com.example.securitywithredis.global.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "인증 API", description = "회원가입 및 토큰 재발급 API")
@RequestMapping("/auth")
public class AuthController {

    private final JoinService joinService;
    private final ReissueService reissueService;

    public AuthController(JoinService joinService, ReissueService reissueService) {
        this.joinService = joinService;
        this.reissueService = reissueService;
    }

    @Operation(summary = "회원가입", description = "Post (username, password, nickname, gender)")
    @PostMapping("/join")
    public ApiResponse<AuthResponseDTO.JoinResultDTO> joinProcess(@RequestBody @Valid AuthRequestDTO.JoinDTO request) {
        UserEntity newUser = joinService.joinProcess(request);
        return ApiResponse.onSuccess(AuthConverter.toJoinResultDTO(newUser));
    }

    @Operation(summary = "토큰 재발행", description = "refresh=refreshToken Cookie 요청 (Swagger에서는 쿠키 테스트 불가능하므로 포스트맨 사용 권장)")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        return reissueService.reissue(request, response);
    }
}