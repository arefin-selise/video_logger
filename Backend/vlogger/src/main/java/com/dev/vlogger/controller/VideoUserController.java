package com.dev.vlogger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.vlogger.dto.AssignData;
import com.dev.vlogger.dto.ReqRes;
import com.dev.vlogger.service.VideoUserService;

@RestController
public class VideoUserController {
	@Autowired
	private VideoUserService videoUserService;

	@PostMapping("/admin/assign")
	public ResponseEntity<ReqRes> assignVideo(@RequestBody AssignData data) {
		return ResponseEntity.ok(videoUserService.assignVideo(data.getUserId(), data.getVideoId()));
	}

	@GetMapping("/user/get-all-videos")
	public ResponseEntity<ReqRes> getAllAssignedVideos() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return ResponseEntity.ok(videoUserService.getAssignedVideos(username));
	}

	@PostMapping("/user/viewed")
	public ResponseEntity<ReqRes> completeVideo(@RequestBody AssignData data) {
		return ResponseEntity.ok(videoUserService.submitViewed(data.getVideoId()));
	}
}
