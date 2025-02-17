package com.example.demo.service;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.UserInfoRequestDto;
import com.example.demo.dto.UserInfoResponseDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.UserRoleEnum;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserInfoResponseDto signup(UserInfoRequestDto reqDto) {
        String username = reqDto.getUsername();
        String nickname = reqDto.getNickname();
        String password = passwordEncoder.encode(reqDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // nickname 중복확인
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임 입니다.");
        }

        // 사용자 등록
        User user = UserInfoRequestDto.toEntity(reqDto, password);
        List<UserRole> UserRoles = new ArrayList<>();

        Role role = roleRepository.findByAuthorityName(UserRoleEnum.USER);
        UserRole userRole = UserRole.builder().user(user).role(role).build();
        userRole = userRoleRepository.save(userRole);
        UserRoles.add(userRole);

        user.setUserRoles(UserRoles);
        userRepository.save(user);

        return UserInfoResponseDto.from(user);
    }

    public LoginResponseDto login(LoginRequestDto reqDto) {
        String username = reqDto.getUsername();
        String password = reqDto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        List<UserRole> userRoles = user.getAuthorities();
        List<Role> roles = userRoles.stream().map(UserRole::getRole).toList(); //UserRole -> Role>
        List<UserRoleEnum> userRoleEnums = roles.stream().map(Role::getAuthorityName).toList(); //Role -> UserRoleEnum

        String token = jwtUtil.createToken(user.getUsername(), userRoleEnums);
        token = jwtUtil.substringToken(token);
        LoginResponseDto resDto = LoginResponseDto.builder().token(token).build();

        return resDto;
    }
}
