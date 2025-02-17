package com.example.demo.dto;

import com.example.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoleIdResponseDto {
    private Long roleId;

    public static RoleIdResponseDto from(Role role) {
        return RoleIdResponseDto.builder()
                .roleId(role.getRoleId())
                .build();
    }
}
