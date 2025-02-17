package com.example.demo.controller;

import com.example.demo.dto.RoleIdResponseDto;
import com.example.demo.dto.RoleInfoRequestDto;
import com.example.demo.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "권한 생성", description = "권한 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신규 권한 생성 성공"),
    })
    @PostMapping
    public RoleIdResponseDto addRole(@RequestBody RoleInfoRequestDto reqDto){

        RoleIdResponseDto resDto = roleService.addRole(reqDto);

        return resDto;
    }
}
