package com.example.demo;

import io.jsonwebtoken.ExpiredJwtException;
import com.example.demo.jwt.JwtUtil;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

// TestInstane는 테스트 인스턴스의 라이프 사이클을 설정할 때 사용
// PER_METHOD: test 함수 당 인스턴스가 생성
// PER_CLASS: test 클래스 당 인스턴스가 생성
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTokenizerTest {
    private static JwtUtil jwtUtil;
    private String secretKey;
    private String base64EncodedSecretKey;

    // 테스트에 사용할 Secret Key를 Base64 형식으로 인코딩한 후, 인코딩된 Secret Key를 각 테스트 케이스에서 사용
    @BeforeAll
    public void init() {
        jwtUtil = new JwtUtil();
        secretKey = "kevin1234123412341234123412341234";  // encoded "a2V2aW4xMjM0MTIzNDEyMzQxMjM0MTIzNDEyMzQxMjM0"

        base64EncodedSecretKey = jwtUtil.encodeBase64SecretKey(secretKey);
    }

    // Plain Text인 Secret Key가 Base64 형식으로 인코딩이 정상적으로 수행되는지 테스트
    @Test
    public void encodeBase64SecretKeyTest() {
        System.out.println(base64EncodedSecretKey);

        assertThat(secretKey, is(new String(Base64.getDecoder().decode(base64EncodedSecretKey))));
    }

    // JwtTokenizer가 Access Token을 정상적으로 생성하는지 테스트
    @Test
    public void generateAccessTokenTest() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expiration = calendar.getTime();

        String accessToken = jwtUtil.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        System.out.println(accessToken);

        assertThat(accessToken, notNullValue());
    }

    // Jwt Tokenizer가 Refresh Token을 정상적으로 생성하는지 테스트
    @Test
    public void generateRefreshTokenTest() {
        String subject = "test refresh token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Date expiration = calendar.getTime();

        String refreshToken = jwtUtil.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        System.out.println(refreshToken);

        assertThat(refreshToken, notNullValue());
    }

    // JwtTokenizer의 verifySignature() 메서드가 Signature를 잘 검증하는지 테스트
    @DisplayName("does not throw any Exception when jws verify")
    @Test
    public void verifySignatureTest() {
        String accessToken = getAccessToken(Calendar.MINUTE, 10);
        assertDoesNotThrow(() -> jwtUtil.verifySignature(accessToken, base64EncodedSecretKey));
    }

    // JWT 생성 시 지정한 만료일시가 지나면 JWT가 정말 만료되는지 테스트
    @DisplayName("throw ExpiredJwtException when jws verify")
    @Test
    public void verifyExpirationTest() throws InterruptedException {
        // 만료 시간을 3초 후로 설정
        String accessToken = getAccessToken(Calendar.SECOND, 3);

        // 바로 검증하면 만료되지 않아야 하므로 예외가 발생하지 않음
        assertDoesNotThrow(() -> jwtUtil.verifySignature(accessToken, base64EncodedSecretKey));

        // 3.5초 대기해서 토큰이 만료되도록 함
        TimeUnit.MILLISECONDS.sleep(3500);

        // 이제 만료되어 ExpiredJwtException이 발생해야 함
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.verifySignature(accessToken, base64EncodedSecretKey));
    }

    private String getAccessToken(int timeUnit, int timeAmount) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, timeAmount);
        Date expiration = calendar.getTime();
        return jwtUtil.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }

}
