package com.example.authsec.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final com.example.authsec.auth.AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        System.out.println("Tentative de connexion pour : " + request.getEmail());
        System.out.println("Mot de passe reçu : " + request.getPassword());
        // Authentifier l'utilisateur et générer une réponse
        AuthenticationResponse response = service.authenticate(request);

        // Si la réponse est null, cela signifie que l'authentification a échoué
        if (response == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AuthenticationResponse("Authentification échouée"));
        }

        // Si l'authentification est réussie, renvoyer la réponse avec le token JWT
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }
}
