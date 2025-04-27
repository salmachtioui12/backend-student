/*package com.example.authsec.user;


import com.example.authsec.stagiaire.StudentProfile;
import com.example.authsec.token.Token;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
//implementation de UserDetails pour  permetre a springboot authentification et autorisation
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)//stocké format string dans bd
    private Role role;

    @OneToMany(mappedBy = "user") //utilisateur peut avoir pls token(principe jwt, date expiration), on dois anoir colonne user_id dans table Token
    private List<Token> tokens;



    @Override
    //Retourne la liste des autorisations de l'utilisateur en fonction de son role
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}*/
package com.example.authsec.user;

import com.example.authsec.stagiaire.StudentProfile;
import com.example.authsec.token.Token;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")

public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Token> tokens;







   /* @OneToOne(mappedBy = "user")
    @JsonIgnore // Ignore complètement la référence au profil
    private StudentProfile profile;

    @OneToMany(mappedBy = "user")
    @JsonIgnore // Ignore aussi la liste des tokens si nécessaire
 
*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities(); // Utilise les authorities du rôle
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}