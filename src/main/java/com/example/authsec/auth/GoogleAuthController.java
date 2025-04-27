package com.example.authsec.auth;

import com.example.authsec.user.Role;
import com.example.authsec.user.User;
import com.example.authsec.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final UserRepository userRepository; // Injecte le repo User

    @GetMapping("/google")
    public Map<String, Object> googleLogin(@AuthenticationPrincipal OidcUser principal) {
        String email = principal.getEmail();
        String name = principal.getFullName();
        String picture = principal.getPicture();

        // Vérifie si l'utilisateur existe déjà
        Optional<User> existingUser = userRepository.findByEmail(email);

        User user;
        if (existingUser.isEmpty()) {
            // Crée un nouvel utilisateur avec un rôle par défaut
            user = User.builder()
                    .email(email)
                    .firstname(name)  // Ou split le nom pour firstname / lastname
                    .password("") // Aucun mot de passe car il vient de Google
                    .role(Role.USER) // Rôle par défaut
                    .build();


            userRepository.save(user); // Sauvegarde en BD
        } else {
            user = existingUser.get();
        }

        // Génère un JWT pour l'utilisateur
        String jwtToken = generateJwtToken(user);

        return Map.of(
                "email", user.getEmail(),
                "name", user.getFirstname(),
                "jwtToken", jwtToken
        );
    }

    private String generateJwtToken(User user) {
        // Logique pour générer un JWT à partir des infos de l'utilisateur
        return "generatedJwtToken"; // Remplace par ta logique de génération
    }
}
