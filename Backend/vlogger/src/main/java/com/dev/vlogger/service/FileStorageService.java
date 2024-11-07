package com.dev.vlogger.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dev.vlogger.config.FileStorageProperties;
import com.dev.vlogger.dto.ReqRes;
import com.dev.vlogger.exception.FileNotFoundException;
import com.dev.vlogger.exception.FileStorageException;
import com.dev.vlogger.model.Video;
import com.dev.vlogger.repository.VideoRepo;

@Service
public class FileStorageService {
	private final Path fileStorageLocation;

	@Autowired
	private VideoRepo videoRepo;

	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);

		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory to upload");
		}
	}

	public String storeFile(MultipartFile file) {
		String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		String fileName = UUID.randomUUID().toString() + "." + getFileExtension(originalFileName);
		try {
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			saveVideo(originalFileName, fileName);

			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file" + fileName + ". Please try again!", ex);
		}
	}

	private void saveVideo(String originalFileName, String fileName) {
		Video video = new Video();
		video.setTitle(originalFileName);
		video.setDescription("Demo Video");
		video.setFileId(fileName);
		videoRepo.save(video);
	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new FileNotFoundException("File not found " + fileName);
		}
	}

	public String getFileExtension(String originalFileName) {
		if (originalFileName != null && originalFileName.contains(".")) {
			return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
		}
		return "";
	}

	public ReqRes getAllVideos() {
		ReqRes reqRes = new ReqRes();

		try {
			List<Video> result = videoRepo.findAll();
			if (!result.isEmpty()) {
				reqRes.setVideoList(result);
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
}
