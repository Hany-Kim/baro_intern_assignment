package com.example.demo.controller;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.UserInfoRequestDto;
import com.example.demo.dto.UserInfoResponseDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
    })
    @PostMapping("/signup")
    public UserInfoResponseDto signup(@RequestBody UserInfoRequestDto reqDto) {
        UserInfoResponseDto resDto = userService.signup(reqDto);
        return resDto;
    }

    @Operation(summary = "로그인", description = "로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
    })
    @PostMapping("/sign")
    public LoginResponseDto login(@RequestBody LoginRequestDto reqDto) {
        LoginResponseDto resDto = userService.login(reqDto);
        return resDto;
    }
}
