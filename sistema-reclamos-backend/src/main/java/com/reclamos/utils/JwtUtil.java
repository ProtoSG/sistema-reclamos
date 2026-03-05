package com.reclamos.utils;

import com.reclamos.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

  // Secret key para firmar los tokens (en producción debería ser una variable de entorno)
  private static final String SECRET_KEY = "sistemareclamosjwtsecretkeydebeteneralmenos256bitsparahs256seguridad";
  
  // 1 hora en milisegundos
  private static final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000;
  
  // 7 días en milisegundos
  private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000;
  
  private static final String TOKEN_TYPE_ACCESS = "ACCESS";
  private static final String TOKEN_TYPE_REFRESH = "REFRESH";

  private static SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
  }

  public static String generateAccessToken(Usuario usuario) {
    return generateToken(usuario, ACCESS_TOKEN_VALIDITY, TOKEN_TYPE_ACCESS);
  }

  public static String generateRefreshToken(Usuario usuario) {
    return generateToken(usuario, REFRESH_TOKEN_VALIDITY, TOKEN_TYPE_REFRESH);
  }

  private static String generateToken(Usuario usuario, long validity, String tokenType) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + validity);

    return Jwts.builder()
        .subject(usuario.getUsername())
        .claim("userId", usuario.getId())
        .claim("tokenType", tokenType)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public static boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static String extractUsername(String token) {
    return extractClaims(token).getSubject();
  }

  public static Integer extractUserId(String token) {
    // Gson deserializa números como Double, necesitamos convertir a Integer
    Number userId = extractClaims(token).get("userId", Number.class);
    return userId != null ? userId.intValue() : null;
  }

  public static String extractTokenType(String token) {
    return extractClaims(token).get("tokenType", String.class);
  }

  public static boolean isTokenExpired(String token) {
    try {
      Date expiration = extractClaims(token).getExpiration();
      return expiration.before(new Date());
    } catch (Exception e) {
      return true;
    }
  }

  public static boolean isAccessToken(String token) {
    return TOKEN_TYPE_ACCESS.equals(extractTokenType(token));
  }

  public static boolean isRefreshToken(String token) {
    return TOKEN_TYPE_REFRESH.equals(extractTokenType(token));
  }

  public static Date extractExpiration(String token) {
    return extractClaims(token).getExpiration();
  }

  private static Claims extractClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

}
