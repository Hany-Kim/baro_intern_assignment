package com.example.demo.controller;

import com.example.demo.dto.RoleIdResponseDto;
import com.example.demo.dto.RoleInfoRequestDto;
import com.example.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public RoleIdResponseDto addRole(@RequestBody RoleInfoRequestDto reqDto){

        RoleIdResponseDto resDto = roleService.addRole(reqDto);

        return resDto;
    }
}
