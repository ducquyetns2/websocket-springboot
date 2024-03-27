package com.ducquyet.websocket.service;

import com.ducquyet.websocket.entity.Token;
import com.ducquyet.websocket.entity.User;
import com.ducquyet.websocket.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtService {
    private final TokenRepository tokenRepository;
    private final UserDetailsServiceImp userDetailsServiceImp;
    private final String encodedKey="8fcdb4db3d70ac3228ae20e174fbfe32065dd2a774e08f70ffefaca220a61f7f";
    private SecretKey generateKey() {
        String secretKey = "Ducquyetns2";
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedKey));
    }
    public Map<String,String> generateClaims(User user) {
        Map<String,String> claims=new HashMap<>();
        claims.put("id",user.getId().toString());
        claims.put("fullName",user.getFullName());
        claims.put("username",user.getUsername());
        claims.put("avatar",user.getAvatar());
        return claims;
    }
    public String generateToken(User user, Map<String,String> claims) {
        final int EXPIRED_HOUR=2;
        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY,EXPIRED_HOUR);
        Date expiredTime=cal.getTime();
        return Jwts.builder().signWith(generateKey())
                .expiration(expiredTime)
                .subject(user.getUsername())
                .claims(claims)
                .compact();
    }
    public Claims extractToken(String token) {
            return Jwts.parser().verifyWith(generateKey()).build()
                    .parseSignedClaims(token).getPayload();
    }
    public UserDetails getUserDetails(String token) {
        var username=extractToken(token).getSubject();
        return userDetailsServiceImp.loadUserByUsername(username);
    }
    public boolean checkOutDateToken(String token) {
        if(extractToken(token) ==null) return false;
        Claims claim=extractToken(token);
        Date expiredTime=claim.getExpiration();
        return expiredTime.after(new Date());
    }
    public boolean validateToken(String token) {
        Optional<Token> tokenEntity=tokenRepository.findByAccessToken(token);
        return checkOutDateToken(token) && tokenEntity.isPresent()
                && !tokenEntity.get().isRevoked();
    }
}
