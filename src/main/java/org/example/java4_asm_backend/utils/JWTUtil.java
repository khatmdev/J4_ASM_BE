package org.example.java4_asm_backend.utils;

import io.jsonwebtoken.*;
import java.util.Date;

public class JWTUtil {

    // Mã bí mật dùng để mã hóa và giải mã token
    private static final String SECRET_KEY = "your_secret_key";

    // Thời gian token hết hạn (ví dụ: 1 giờ)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 8;

    /**
     * Hàm tạo JWT token với username và thời gian hết hạn.
     * @param username Tên người dùng
     * @return JWT token đã được tạo
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Đặt username vào payload
                .setIssuedAt(new Date()) // Thời điểm token được phát hành
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Thời gian hết hạn token
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // Sử dụng thuật toán HS512 để ký token
                .compact();
    }

    /**
     * Hàm giải mã JWT token và lấy ra username.
     * @param token JWT token
     * @return Tên người dùng (subject)
     */
    public static String decodeToken(String token) {
        try {
            // Giải mã và lấy thông tin từ token
            Jws<Claims> claims = Jwts.parser() // Dùng parser() thay vì parserBuilder()
                    .setSigningKey(SECRET_KEY) // Cung cấp khóa bí mật để xác thực
                    .parseClaimsJws(token);

            // Lấy subject (username) từ claims
            return claims.getBody().getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }

    /**
     * Hàm kiểm tra token có hợp lệ hay không.
     * @param token JWT token
     * @return true nếu token hợp lệ, false nếu không hợp lệ
     */
    public static boolean isTokenValid(String token) {
        try {
            // Kiểm tra tính hợp lệ của token (bao gồm cả thời gian hết hạn)
            Jws<Claims> claims = Jwts.parser() // Dùng parser() thay vì parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

            // Kiểm tra xem token đã hết hạn chưa
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            return false; // Token không hợp lệ hoặc đã hết hạn
        }
    }
}
