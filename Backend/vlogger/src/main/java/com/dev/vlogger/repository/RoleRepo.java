package com.dev.vlogger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.vlogger.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
	Role findRoleByName(String name);
}
