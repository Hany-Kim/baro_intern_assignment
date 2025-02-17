package com.example.demo.controller;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.UserInfoRequestDto;
import com.example.demo.dto.UserInfoResponseDto;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public UserInfoResponseDto signup(@RequestBody UserInfoRequestDto reqDto) {
        UserInfoResponseDto resDto = userService.signup(reqDto);
        return resDto;
    }

    @PostMapping("/sign")
    public LoginResponseDto login(@RequestBody LoginRequestDto reqDto) {
        LoginResponseDto resDto = userService.login(reqDto);
        return resDto;
    }
}
