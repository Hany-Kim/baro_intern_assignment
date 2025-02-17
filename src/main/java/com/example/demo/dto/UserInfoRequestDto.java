package com.example.demo.dto;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDto {
    private String username;
    private String password;
    private String nickname;

    public static User toEntity(UserInfoRequestDto reqDto, String password) {
        return User.builder()
                .username(reqDto.getUsername())
                .password(password)
                .nickname(reqDto.getNickname())
                .build();
    }
}
