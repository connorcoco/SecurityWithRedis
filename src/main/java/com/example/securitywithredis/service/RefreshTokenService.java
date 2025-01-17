package com.example.securitywithredis.service;

import com.example.securitywithredis.apiPayload.code.status.ErrorStatus;
import com.example.securitywithredis.apiPayload.exception.GeneralException;
import com.example.securitywithredis.util.RefreshUtil;
import com.example.securitywithredis.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    private final JWTUtil jwtUtil;
    private final RefreshUtil refreshUtil;

    public RefreshTokenService(JWTUtil jwtUtil, RefreshUtil refreshUtil) {
        this.jwtUtil = jwtUtil;
        this.refreshUtil = refreshUtil;
    }

    // Refresh Token을 요청에서 추출
    public String extractRefreshToken(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
        return refresh;
    }

    // Refresh Token 유효성 검사
    public void validateRefreshToken(String refresh) {
        try {
            jwtUtil.isExpired(refresh); // Expired check
            String category = jwtUtil.getCategory(refresh);
            if (!category.equals("refresh")) {
                throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN); // Refresh 토큰이 아닌 경우
            }
            // Redis에서 존재 여부 확인
            String storedRefreshToken = refreshUtil.getRefreshToken(refresh);
            if (storedRefreshToken == null || !storedRefreshToken.equals(refresh)) {
                throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN); // Redis에서 없거나 일치하지 않으면
            }
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_EXPIRED); // Expired Token 예외 처리
        }
    }

    // Refresh Token을 Redis에서 제거
    public void removeRefreshToken(String refresh) {
        refreshUtil.removeRefreshToken(refresh); // Redis에서 제거
    }

    // Refresh Token을 Redis에 저장
    public void addRefreshToken(String username, String refreshToken, long expirationTimeInMillis) {
        refreshUtil.addRefreshToken(username, refreshToken, expirationTimeInMillis);
    }
}