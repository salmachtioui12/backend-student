package com.example.authsec.stagiaire;

import com.example.authsec.user.User;
import com.example.authsec.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class StudentProfileService {

    @Autowired
    private StudentProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public StudentProfile createProfile(StudentProfile profile, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        profile.setUser(user);
        // Lier les sous-entités avec le profil étudiant
        for (StudentEducation education : profile.getEducations()) {
            education.setStudentProfile(profile);
        }

        for (StudentSkill skill : profile.getSkills()) {
            skill.setStudentProfile(profile);
        }

        for (StudentCertification certification : profile.getCertifications()) {
            certification.setStudentProfile(profile);
        }

        for (StudentExperience experience : profile.getExperiences()) {
            experience.setStudentProfile(profile);
        }

        return profileRepository.save(profile);
    }

    public StudentProfile updateProfile(Integer id, StudentProfile updatedProfile, String userEmail) {
        StudentProfile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil non trouvé"));

        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!existingProfile.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Action non autorisée");
        }

        // Mise à jour sélective
        existingProfile.setHeadline(updatedProfile.getHeadline());
        existingProfile.setSummary(updatedProfile.getSummary());
        existingProfile.setLocation(updatedProfile.getLocation());
        existingProfile.setPhone(updatedProfile.getPhone());

        // Mise à jour des entités liées
        existingProfile.getEducations().clear();
        existingProfile.getSkills().clear();
        existingProfile.getCertifications().clear();
        existingProfile.getExperiences().clear();

        for (StudentEducation education : updatedProfile.getEducations()) {
            education.setStudentProfile(existingProfile);
            existingProfile.getEducations().add(education);
        }

        for (StudentSkill skill : updatedProfile.getSkills()) {
            skill.setStudentProfile(existingProfile);
            existingProfile.getSkills().add(skill);
        }

        for (StudentCertification certification : updatedProfile.getCertifications()) {
            certification.setStudentProfile(existingProfile);
            existingProfile.getCertifications().add(certification);
        }

        for (StudentExperience experience : updatedProfile.getExperiences()) {
            experience.setStudentProfile(existingProfile);
            existingProfile.getExperiences().add(experience);
        }

        return profileRepository.save(existingProfile);
    }
    public StudentProfile getProfile(Integer id, String userEmail) {
        // Vérifiez si le profil existe
        StudentProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil non trouvé"));

        // Vérifiez que l'utilisateur correspond au profil
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Action non autorisée");
        }

        return profile;
    }
    public Map<String, String> getProfileFiles(Integer id, String userEmail) {
        // Récupérer le profil étudiant par ID
        StudentProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil non trouvé"));

        // Vérifier si l'utilisateur a accès au profil
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Action non autorisée");
        }

        // Créer une map des fichiers associés au profil
        Map<String, String> files = new HashMap<>();
        files.put("cv", profile.getCvPath());
        files.put("motivationLetter", profile.getMotivationLetterPath());
        files.put("profilePicture", profile.getProfilePicture());
        files.put("coverPhoto", profile.getCoverPhoto());

        return files;
    }
    @Transactional
    public void deleteProfile(Integer id, String userEmail) {
        // 1. Charger le profil avec TOUTES ses associations
        StudentProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil non trouvé"));

        // 2. Vérification des droits
        if (!profile.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Action non autorisée");
        }

        // 3. Suppression en cascade automatique grâce à JPA
        profileRepository.delete(profile);


        // 4. Forcer l'exécution immédiate
        profileRepository.flush();

        // 5. Vérification (optionnel - pour le debug)
        if (profileRepository.existsById(id)) {
            throw new RuntimeException("La suppression a échoué");
        }
    }
    public Optional<StudentProfile> getProfileByUserEmail(String email) {
        return profileRepository.findByUserEmail(email);
    }

    public Optional<StudentProfile> getProfileByUserId(Integer userId) {
        return profileRepository.findByUserId(userId);
}


}

