package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserInfoRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 각 테스트 후 롤백
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void testSignup_Success() throws Exception {
        UserInfoRequestDto signupRequest = UserInfoRequestDto.builder()
                .username("testuser")
                .nickname("Test User")
                .password("password")
                .build();

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                // 응답 JSON에 username과 nickname 필드가 있는지 확인
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.nickname").value("Test User"));
    }

    @Test
    @DisplayName("회원가입 - 중복 사용자 에러 테스트")
    public void testSignup_DuplicateUsername() throws Exception {
        // 최초 회원가입
        UserInfoRequestDto signupRequest = UserInfoRequestDto.builder()
                .username("duplicateuser")
                .nickname("User One")
                .password("password")
                .build();

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());

        // 동일 username으로 다시 회원가입 시도 (닉네임은 다르게)
        UserInfoRequestDto signupRequest2 = UserInfoRequestDto.builder()
                .username("duplicateuser")
                .nickname("User Two")
                .password("password")
                .build();

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest2)))
                // (프로젝트의 예외 핸들링 정책에 따라 조정)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void testLogin_Success() throws Exception {
        // 먼저 회원가입
        UserInfoRequestDto signupRequest = UserInfoRequestDto.builder()
                .username("loginuser")
                .nickname("Login User")
                .password("password")
                .build();

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());

        // 로그인 요청
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("loginuser");
        loginRequest.setPassword("password");

        mockMvc.perform(post("/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                // LoginResponseDto에 token 필드가 존재하는지 확인
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("로그인 - 비밀번호 불일치 에러 테스트")
    public void testLogin_InvalidPassword() throws Exception {
        // 먼저 회원가입
        UserInfoRequestDto signupRequest = UserInfoRequestDto.builder()
                .username("invalidloginuser")
                .nickname("Invalid Login User")
                .password("password")
                .build();

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());

        // 잘못된 비밀번호로 로그인 시도
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("invalidloginuser");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                // IllegalArgumentException 발생 시 클라이언트 에러(예, 400)를 기대
                .andExpect(status().isBadRequest());
    }
}
