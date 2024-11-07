package com.dev.vlogger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.vlogger.model.Role;
import com.dev.vlogger.repository.RoleRepo;

@Service
public class RoleService {
	@Autowired
	private RoleRepo roleRepo;

	public Role findByName(String name) {
		Role role = roleRepo.findRoleByName(name);
		return role;
	}
}
