package com.example.authsec.stagiaire;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profiles")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentProfileController {

    @Autowired
    private StudentProfileService profileService;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<StudentProfile> createProfile(
            @RequestParam("profile") String profileJson, // Le profil au format JSON
            @RequestParam("cv") MultipartFile cvFile,  // CV
            @RequestParam("letter") MultipartFile letterFile,  // Lettre de motivation
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,  // Photo de profil (optionnelle)
            @RequestParam(value = "coverPhoto", required = false) MultipartFile coverPhoto,  // Photo de couverture (optionnelle)
            Principal principal) {

        try {
            // Convertir le profil JSON en un objet StudentProfile
            StudentProfile profile = new ObjectMapper().readValue(profileJson, StudentProfile.class);

            // Sauvegarder les fichiers dans le répertoire approprié
            String basePath = new File(".").getCanonicalPath();
            String uploadDir = basePath + File.separator + "uploads" + File.separator + "student";

            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String cvPath = uploadDir + "/cv_" + System.currentTimeMillis() + "_" + cvFile.getOriginalFilename();
            String letterPath = uploadDir + "/letter_" + System.currentTimeMillis() + "_" + letterFile.getOriginalFilename();
            String profilePicPath = null;
            String coverPicPath = null;

            cvFile.transferTo(new File(cvPath));
            letterFile.transferTo(new File(letterPath));

            if (profilePicture != null && !profilePicture.isEmpty()) {
                profilePicPath = uploadDir + "/profile_" + System.currentTimeMillis() + "_" + profilePicture.getOriginalFilename();
                profilePicture.transferTo(new File(profilePicPath));
            }

            if (coverPhoto != null && !coverPhoto.isEmpty()) {
                coverPicPath = uploadDir + "/cover_" + System.currentTimeMillis() + "_" + coverPhoto.getOriginalFilename();
                coverPhoto.transferTo(new File(coverPicPath));
            }

            // Sauvegarder le profil avec les chemins des fichiers
            profile.setCvPath(cvPath);
            profile.setMotivationLetterPath(letterPath);
            if (profilePicPath != null) profile.setProfilePicture(profilePicPath);
            if (coverPicPath != null) profile.setCoverPhoto(coverPicPath);

            // Appel du service pour créer le profil
            return ResponseEntity.ok(profileService.createProfile(profile, principal.getName()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<StudentProfile> updateProfile(
            @PathVariable Integer id,
            @RequestParam("profile") String profileJson, // Le profil au format JSON
            @RequestParam("cv") MultipartFile cvFile,  // CV
            @RequestParam("letter") MultipartFile letterFile,  // Lettre de motivation
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,  // Photo de profil (optionnelle)
            @RequestParam(value = "coverPhoto", required = false) MultipartFile coverPhoto,  // Photo de couverture (optionnelle)
            Principal principal) {

        try {
            // Convertir le profil JSON en un objet StudentProfile
            StudentProfile updatedProfile = new ObjectMapper().readValue(profileJson, StudentProfile.class);

            // Sauvegarder les fichiers dans le répertoire approprié
            String basePath = new File(".").getCanonicalPath();
            String uploadDir = basePath + File.separator + "uploads" + File.separator + "student";

            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String cvPath = uploadDir + "/cv_" + System.currentTimeMillis() + "_" + cvFile.getOriginalFilename();
            String letterPath = uploadDir + "/letter_" + System.currentTimeMillis() + "_" + letterFile.getOriginalFilename();
            String profilePicPath = null;
            String coverPicPath = null;

            cvFile.transferTo(new File(cvPath));
            letterFile.transferTo(new File(letterPath));

            if (profilePicture != null && !profilePicture.isEmpty()) {
                profilePicPath = uploadDir + "/profile_" + System.currentTimeMillis() + "_" + profilePicture.getOriginalFilename();
                profilePicture.transferTo(new File(profilePicPath));
            }

            if (coverPhoto != null && !coverPhoto.isEmpty()) {
                coverPicPath = uploadDir + "/cover_" + System.currentTimeMillis() + "_" + coverPhoto.getOriginalFilename();
                coverPhoto.transferTo(new File(coverPicPath));
            }

            // Mettre à jour le profil avec les chemins des fichiers
            updatedProfile.setCvPath(cvPath);
            updatedProfile.setMotivationLetterPath(letterPath);
            if (profilePicPath != null) updatedProfile.setProfilePicture(profilePicPath);
            if (coverPicPath != null) updatedProfile.setCoverPhoto(coverPicPath);

            // Appel du service pour mettre à jour le profil
            return ResponseEntity.ok(profileService.updateProfile(id, updatedProfile, principal.getName()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentProfile> getProfile(@PathVariable Integer id, Principal principal) {
        try {
            // Récupérer l'email de l'utilisateur connecté (principal)
            String userEmail = principal.getName();

            // Appel du service pour récupérer le profil
            StudentProfile profile = profileService.getProfile(id, userEmail);

            // Retourner la réponse avec le profil
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/{id}/files")
    public ResponseEntity<Map<String, String>> getProfileFiles(@PathVariable Integer id, Principal principal) {
        try {
            // Récupérer l'email de l'utilisateur connecté (principal)
            String userEmail = principal.getName();

            // Appel du service pour récupérer les fichiers
            Map<String, String> files = profileService.getProfileFiles(id, userEmail);

            // Retourner la réponse avec les chemins des fichiers
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(
            @PathVariable Integer id,
            Principal principal) {
        try {
            profileService.deleteProfile(id, principal.getName());
            return ResponseEntity.ok("Profil supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}
    // Les autres méthodes restent inchangées

