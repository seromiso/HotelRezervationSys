package com.finartzIntern.HotelRezervationSys.domain.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserRole;
import com.finartzIntern.HotelRezervationSys.domain.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING) // Veritabanına "ADMIN", "MANAGER" şeklinde metin olarak yazar
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "datebirth", nullable = false)
    private LocalDate dateBirth; // Şemadaki datetime karşılığı

    @Column(name = "phonenumber", length = 20) //
    private String phoneNumber;

    @Column(name = "email_verified", nullable = false)
    @JsonProperty("email_verified")
    private boolean emailVerified;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private UserStatus status; // ACTIVE, INACTIVE



    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    // Spring Security şifre doğrulama için bu metodu kullanacak
    @Override
    public String getPassword() {
        return this.passwordHash; // Veritabanındaki hashlenmiş şifre alanı
    }

    // Kullanıcı adı olarak e-posta adresini kullanıyoruz
    @Override
    public String getUsername() {
        return this.email;
    }

    // Hesap durum kontrolleri (Şimdilik hepsini true dönebiliriz)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    // Eğer status PENDING_APPROVAL veya PASSIVE ise girişi engellemek için burayı bağlayabiliriz!
    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(this.status);
    }
}
