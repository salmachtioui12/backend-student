package com.example.authsec.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    //optional evite les valeurs null
    Optional<User> findByEmail(String email);

    //deux senarios possible :
    //1- user pas trouvé => Optional<User> contient l'utilisateur.
    //2- user trouvé => Optional<User>.empty() évite une NullPointerException
}