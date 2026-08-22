package com.finartzIntern.HotelRezervationSys.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authProvider);
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // Eğer passwordEncoder metodu burada değilse, sınıfa inject ettiğin değişkeni ver: passwordEncoder
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Giriş yapma, kayıt olma ve onay mailine tıklama (/verify) herkese açık olsun
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // 2. İŞTE KİLİT NOKTA: Profil, rezervasyon vb. sadece e-postası onaylı kullanıcılara açılsın!
                        .requestMatchers("/api/v1/users/**", "/api/v1/reservations/**").hasAuthority("VERIFIED_USER")

                        // 3. Geriye kalan diğer tüm endpointler için sadece sisteme giriş yapmış (login) olmak yetsin
                        .anyRequest().authenticated()
                )
                // JWT stateless (durumsuz) çalıştığı için sunucuda session (oturum) tutulmasını engelliyoruz
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                )
                // Veritabanı kimlik doğrulama sağlayıcımızı (DaoAuthenticationProvider) tanıtıyoruz
                .authenticationProvider(authenticationProvider())
                // Bizim yazdığımız JWT filtresini, Spring Security'nin standart kullanıcı adı-şifre kontrol filtresinden ÖNCE çalıştır diyoruz
                .addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
