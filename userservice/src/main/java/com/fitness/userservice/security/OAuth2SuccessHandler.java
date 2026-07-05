package com.fitness.userservice.security;

import com.fitness.userservice.models.AuthProvider;
import com.fitness.userservice.models.User;
import com.fitness.userservice.models.UserRole;
import com.fitness.userservice.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        // 1. Google user
        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        // 2. Read Google data
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
//        String picture = oauthUser.getAttribute("picture");

        // 3. Find user
        User user = userRepository.findByEmail(email).orElse(null);

        // 4. First login
        if (user == null) {

            user = new User();

            user.setEmail(email);
            String[] namePart = name.split(" ");
            user.setFirstName(namePart[0]);
            user.setLastName(namePart[1]);
            // Google user ke paas password nahi hota // & hamare nullable=false hai
            user.setPassword(UUID.randomUUID().toString());
            user.setRole(UserRole.USER);
            user.setProvider(AuthProvider.GOOGLE);

            userRepository.save(user);
        }

        // 5. Generate JWT
        String jwt =
                jwtService.generateToken(user.getEmail());

        // 6. Redirect frontend
        ResponseCookie cookie =
                ResponseCookie.from("access_token", jwt)
                        .httpOnly(true)
                        .secure(false)      // localhost
                        .path("/")
                        .maxAge(Duration.ofHours(1))
                        .sameSite("Lax")
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString());

        response.sendRedirect("http://localhost:3000/activities");
    }
}