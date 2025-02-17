package com.example.demo.dto;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.UserRoleEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponseDto {
    private String username;
    private String nickname;
    private List<RoleTypeDto> authorities;

    public static UserInfoResponseDto from(User user) {
        List<Role> roles = user.getAuthorities().stream().map(UserRole::getRole).toList(); //UserRole -> Role>
        List<UserRoleEnum> userRoleEnums = roles.stream().map(Role::getAuthorityName).toList(); //Role -> UserRoleEnum>
        List<RoleTypeDto> authorities = new ArrayList<>();
        for(UserRoleEnum role : userRoleEnums) {
            authorities.add(RoleTypeDto.builder().authorityName(role.getAuthority()).build());
        }

        return UserInfoResponseDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .authorities(authorities)
                .build();
    }
}
