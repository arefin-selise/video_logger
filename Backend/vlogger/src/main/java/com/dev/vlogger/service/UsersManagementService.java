package com.dev.vlogger.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dev.vlogger.config.JWTUtils;
import com.dev.vlogger.dto.ReqRes;
import com.dev.vlogger.model.Role;
import com.dev.vlogger.model.User;
import com.dev.vlogger.repository.UserRepo;

@Service
public class UsersManagementService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleService roleService;
	@Autowired
	private JWTUtils jwtUtils;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public ReqRes register(ReqRes registrationRequest) {
		ReqRes resp = new ReqRes();

		try {
			User user = new User();
			user.setEmail(registrationRequest.getEmail());
			user.setName(registrationRequest.getName());
			user.setPhone(registrationRequest.getPhone());
			user.setUsername(registrationRequest.getUsername());
			user.setDisable(0);
			user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

			Role role = roleService.findByName("USER");
			List<Role> roleSet = new ArrayList<>();
			roleSet.add(role);

			if (registrationRequest.getEmail().split("@")[1].equals("admin.vlog")) {
				role = roleService.findByName("ADMIN");
				roleSet.add(role);
			}

			user.setRole(roleSet);

			User userResult = userRepo.save(user);
			if (userResult.getId() > 0) {
				resp.setUser(userResult);
				resp.setMessage("User Saved Successfully");
				resp.setStatusCode(200);
			}

		} catch (Exception e) {
			resp.setStatusCode(500);
			resp.setError(e.getMessage());
		}
		return resp;
	}

	public ReqRes login(ReqRes loginRequest) {
		ReqRes response = new ReqRes();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			var user = userRepo.findByUsername(loginRequest.getUsername()).orElseThrow();
			if (isEnabled(user)) {
				var jwt = jwtUtils.generateToken(user);
				var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
				response.setStatusCode(200);
				response.setToken(jwt);
				response.setEmail(user.getEmail());
				response.setUsername(user.getUsername());
				response.setUser(user);
				response.setRole(getUserRole(user.getRole()));
				response.setRefreshToken(refreshToken);
				response.setExpirationTime("24Hrs");
				response.setMessage("Successfully Logged In");
			} else {
				response.setStatusCode(401);
				response.setMessage("User is inactive!");
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage(e.getMessage());
		}
		return response;
	}

	private String getUserRole(List<Role> role) {
		if (role.size() > 1) {
			return "ADMIN";
		}
		return "USER";
	}

	public ReqRes refreshToken(ReqRes refreshTokenReqiest) {
		ReqRes response = new ReqRes();
		try {
			String username = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
			User users = userRepo.findByUsername(username).orElseThrow();
			if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
				var jwt = jwtUtils.generateToken(users);
				response.setStatusCode(200);
				response.setToken(jwt);
				response.setRefreshToken(refreshTokenReqiest.getToken());
				response.setExpirationTime("24Hr");
				response.setMessage("Successfully Refreshed Token");
			}
			response.setStatusCode(200);
			return response;

		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	public ReqRes getAllUsers() {
		ReqRes reqRes = new ReqRes();

		try {
			List<User> result = userRepo.findAll();
			if (!result.isEmpty()) {
				reqRes.setUsersList(result);
				reqRes.setStatusCode(200);
				reqRes.setMessage("Successful");
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("No users found");
			}
			return reqRes;
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error occurred: " + e.getMessage());
			return reqRes;
		}
	}

	public ReqRes getUsersById(Long id) {
		ReqRes reqRes = new ReqRes();
		try {
			User usersById = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
			reqRes.setUser(usersById);
			reqRes.setStatusCode(200);
			reqRes.setMessage("Users with id '" + id + "' found successfully");
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error occurred: " + e.getMessage());
		}
		return reqRes;
	}

	public ReqRes deleteUser(Long userId) {
		ReqRes reqRes = new ReqRes();
		try {
			Optional<User> userOptional = userRepo.findById(userId);
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				if (user.getEmail().split("@")[1].equals("admin.vlog")) {
					reqRes.setStatusCode(401);
					reqRes.setMessage("Can't delete admin user!");
				} else {
					user.setDisable(1);
					userRepo.save(user);
					reqRes.setStatusCode(200);
					reqRes.setMessage("User deleted successfully");
				}
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("User not found for deletion");
			}
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
		}
		return reqRes;
	}

	public ReqRes updateUser(Long userId, User updatedUser) {
		ReqRes reqRes = new ReqRes();
		try {
			Optional<User> userOptional = userRepo.findById(userId);
			if (userOptional.isPresent()) {
				User existingUser = userOptional.get();
				existingUser.setEmail(updatedUser.getEmail());
				existingUser.setName(updatedUser.getName());
				existingUser.setPhone(updatedUser.getPhone());
				existingUser.setUsername(updatedUser.getUsername());
				existingUser.setRole(existingUser.getRole());

				// Check if password is present in the request
				if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
					// Encode the password and update it
					existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
				}

				User savedUser = userRepo.save(existingUser);
				reqRes.setUser(savedUser);
				reqRes.setStatusCode(200);
				reqRes.setMessage("User updated successfully");
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("User not found for update");
			}
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
		}
		return reqRes;
	}

	public ReqRes getMyInfo(String username) {
		ReqRes reqRes = new ReqRes();
		try {
			Optional<User> userOptional = userRepo.findByUsername(username);
			if (userOptional.isPresent()) {
				reqRes.setUser(userOptional.get());
				reqRes.setStatusCode(200);
				reqRes.setMessage("successful");
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("User not found for update");
			}

		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
		}
		return reqRes;

	}

	public boolean isEnabled(User user) {
		if (user.getDisable() == 1) {
			return false;
		}
		return true;
	}
}
