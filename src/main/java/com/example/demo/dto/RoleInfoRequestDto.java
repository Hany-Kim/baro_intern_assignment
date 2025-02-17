package com.example.demo.dto;

import com.example.demo.entity.Role;
import com.example.demo.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoRequestDto {
    private UserRoleEnum authorityName;

    public static Role toEntity(RoleInfoRequestDto reqDto) {
        return Role.builder()
                .authorityName(reqDto.getAuthorityName())
                .build();
    }
}
