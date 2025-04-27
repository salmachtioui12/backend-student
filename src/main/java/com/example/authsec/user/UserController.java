package com.example.authsec.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController //la class est un controleur qui gère les requêtes HTTP et renvoie des objets JSON au lieu des pages html
//prefixe des routes de ce controlleur
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    //injection de class UserService dans la class UserController sans creer le constructeur c est fait par @RequiredArgsConstructor
    private final UserService service;

    // la requete patch pour modifier partielement une ressource(on change que le password )
    @PatchMapping
    //request contient les 3 para currentPassword, newPassword, confirmationPassword
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        //on fait appel a la method changePassword de classe userservice responsable de changement de password
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}