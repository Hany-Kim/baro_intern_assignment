package com.example.demo.service;

import com.example.demo.dto.RoleInfoRequestDto;
import com.example.demo.dto.RoleIdResponseDto;
import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public RoleIdResponseDto addRole(RoleInfoRequestDto reqDto) {

        Role role = RoleInfoRequestDto.toEntity(reqDto);
        roleRepository.save(role);

        return RoleIdResponseDto.from(role);
    }
}
