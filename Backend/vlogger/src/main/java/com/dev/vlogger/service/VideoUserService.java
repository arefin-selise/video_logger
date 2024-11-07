package com.dev.vlogger.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.vlogger.dto.ReqRes;
import com.dev.vlogger.model.User;
import com.dev.vlogger.model.Video;
import com.dev.vlogger.model.VideoUser;
import com.dev.vlogger.repository.UserRepo;
import com.dev.vlogger.repository.VideoRepo;
import com.dev.vlogger.repository.VideoUserRepo;

@Service
public class VideoUserService {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private VideoRepo videoRepo;
	@Autowired
	private VideoUserRepo videoUserRepo;

	public ReqRes assignVideo(Long userId, Long videoId) {
		ReqRes resp = new ReqRes();

		try {
			VideoUser videoUser = new VideoUser();
			User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User Not found"));
			Video video = videoRepo.findById(videoId).orElseThrow(() -> new RuntimeException("Video Not found"));
			videoUser.setTitle(video.getTitle());
			videoUser.setFileId(video.getFileId());
			videoUser.setUsername(user.getName());
			videoUser.setUserId(userId);
			videoUser.setVideoId(videoId);
			videoUser.setViewed(0);

			VideoUser result = videoUserRepo.save(videoUser);
			if (result.getId() > 0) {
				resp.setVideoUser(result);
				resp.setMessage("VideoUser Saved Successfully");
				resp.setStatusCode(200);
			}
		} catch (Exception e) {
			resp.setStatusCode(500);
			resp.setError(e.getMessage());
		}
		return resp;
	}

	public ReqRes getAssignedVideos(String username) {
		ReqRes reqRes = new ReqRes();

		try {
			Optional<User> userOptional = userRepo.findByUsername(username);
			List<VideoUser> result = videoUserRepo.findAllByUserId(userOptional.get().getId());
			if (!result.isEmpty()) {
				reqRes.setAssignedList(result);
				reqRes.setStatusCode(200);
				reqRes.setMessage("Successful");
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("No videos found");
			}
			return reqRes;
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error occurred: " + e.getMessage());
			return reqRes;
		}
	}

	public ReqRes submitViewed(Long id) {
		ReqRes reqRes = new ReqRes();
		try {
			Optional<VideoUser> optional = videoUserRepo.findById(id);
			if (optional.isPresent()) {
				VideoUser existingItem = optional.get();
				existingItem.setViewed(1);

				VideoUser savedItem = videoUserRepo.save(existingItem);
				reqRes.setVideoUser(savedItem);
				reqRes.setStatusCode(200);
				reqRes.setMessage("Viewing Complete");
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("Video not found for complete");
			}
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error occurred while updating record: " + e.getMessage());
		}
		return reqRes;
	}
}
