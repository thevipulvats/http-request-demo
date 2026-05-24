package com.engineeringdigest.http_request_demo.controller;


import com.engineeringdigest.http_request_demo.dto.AuthRequest;
import com.engineeringdigest.http_request_demo.model.RefreshToken;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.service.CustomUserDetailsService;
import com.engineeringdigest.http_request_demo.service.JwtService;
import com.engineeringdigest.http_request_demo.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest){
        log.info("Login attempt for user: {}", authRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken((User) userDetails);
        log.info("Login successful for user: {}", authRequest.getUsername());
        return ResponseEntity.ok(Map.of("accessToken", token, "refreshToken", refreshToken.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request){
        String refreshToken = request.get("refreshToken");
        refreshTokenService.logoutByToken(refreshToken);
        log.info("Logout processed");
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        String tokenValue = request.get("refreshToken");
        RefreshToken validatedToken = refreshTokenService.verifyRefreshToken(tokenValue);
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(validatedToken);
        String newAccessToken = jwtService.generateToken(validatedToken.getUser());
        log.info("Token refreshed for user: {}", validatedToken.getUser().getName());
        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken.getToken()
        ));
    }



}
