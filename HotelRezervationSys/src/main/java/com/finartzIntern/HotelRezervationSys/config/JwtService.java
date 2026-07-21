package com.finartzIntern.HotelRezervationSys.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // application.properties dosyandan çekeceğimiz en az 256-bit uzunluğunda gizli anahtar
    // Şimdilik test için buraya varsayılan bir değer de bağlayabiliriz.
    @Value("${application.security.jwt.secret-key:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    // Token geçerlilik süresi (Örn: 24 saat = 86400000 milisaniye)
    @Value("${application.security.jwt.expiration:86400000}")
    private long jwtExpiration;

    // 1. Kullanıcı Bilgilerinden Token Üretme (Bilet Basma)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername()) // E-posta adresini yerleştiriyoruz
                .issuedAt(new Date(System.currentTimeMillis())) // Üretim tarihi
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Son kullanma tarihi
                .signWith(getSignInKey(), Jwts.SIG.HS256) // 0.12.6 için yeni imzalama standardı
                .compact();
    }

    // 2. Token'ın Geçerli Olup Olmadığını Kontrol Etme (Barkod Okuma)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Token'daki e-posta ile gelen kullanıcının e-postası uyuşuyor mu ve süresi geçmiş mi?
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 3. Token'ın İçinden Kullanıcı Adını (E-postayı) Çekme
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 4. Token Süresi Dolmuş mu Kontrolü
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // --- JWT Çözümleme Yardımcı Metotları (JJWT 0.12.6 Standardı) ---

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // 0.12.6 ile gelen yeni doğrulama metodu
                .build()
                .parseSignedClaims(token)
                .getPayload(); // Eski sürümdeki getBody() yerine getPayload() kullanıyoruz
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}