package com.finartzIntern.HotelRezervationSys.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. İstekteki "Authorization" başlığını (header) alıyoruz
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Eğer başlık boşsa veya "Bearer " ile başlamıyorsa istek sonraki filtrelere geçsin (işlem yapma)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. "Bearer " metnini kesip saf token'ı alıyoruz (7. karakterden sonrası)
        jwt = authHeader.substring(7);

        // 4. Token'ın içinden kullanıcının e-posta adresini çözüyoruz
        userEmail = jwtService.extractUsername(jwt);

        // 5. E-posta boş değilse ve sistemde hali hazırda giriş yapmış bir kullanıcı yoksa (authentication == null)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Veri tabanından (CustomUserDetailsService aracılığıyla) kullanıcıyı buluyoruz
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Token geçerli mi diye kontrol ediyoruz
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Spring Security için "Bu kullanıcı doğrulandı" kartı (Token) oluşturuyoruz
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities() // Kullanıcının rolleri (CUSTOMER, GUEST vb.)
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7. Ve bu kartı Spring Security'nin çekmecesine (Context) koyuyoruz.
                // Artık bu istek için bodyguard "Geçebilirsin" diyecek.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // İşlemler bitti, isteği bir sonraki filtreye veya Controller'a gönderiyoruz
        filterChain.doFilter(request, response);
    }
}

