package com.example.demo.repository;

import com.example.demo.entity.UserRole;
import com.example.demo.entity.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
