package com.dev.vlogger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.vlogger.dto.ReqRes;
import com.dev.vlogger.model.User;
import com.dev.vlogger.service.UsersManagementService;

@RestController
public class UserManagementController {
	@Autowired
	private UsersManagementService usersManagementService;

	@GetMapping(value = "/public/ping")
	public String ping() {
		return "Application is running, anyone can read this!";
	}

	@PostMapping("/auth/register")
	public ResponseEntity<ReqRes> regeister(@RequestBody ReqRes reg) {
		return ResponseEntity.ok(usersManagementService.register(reg));
	}

	@PostMapping("/auth/login")
	public ResponseEntity<ReqRes> login(@RequestBody ReqRes req) {
		return ResponseEntity.ok(usersManagementService.login(req));
	}

	@PostMapping("/auth/refresh")
	public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req) {
		return ResponseEntity.ok(usersManagementService.refreshToken(req));
	}

	@GetMapping("/admin/get-all-users")
	public ResponseEntity<ReqRes> getAllUsers() {
		return ResponseEntity.ok(usersManagementService.getAllUsers());

	}

	@GetMapping("/admin/get-users/{userId}")
	public ResponseEntity<ReqRes> getUSerByID(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(usersManagementService.getUsersById(userId));

	}

	@PutMapping("/admin/update/{userId}")
	public ResponseEntity<ReqRes> updateUser(@PathVariable("userId") Long userId, @RequestBody User reqres) {
		return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
	}

	@GetMapping("/adminuser/get-profile")
	public ResponseEntity<ReqRes> getMyProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		ReqRes response = usersManagementService.getMyInfo(username);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}

	@DeleteMapping("/admin/delete/{userId}")
	public ResponseEntity<ReqRes> deleteUSer(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(usersManagementService.deleteUser(userId));
	}

}