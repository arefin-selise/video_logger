package com.dev.vlogger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.vlogger.model.VideoUser;

public interface VideoUserRepo extends JpaRepository<VideoUser, Long> {
	List<VideoUser> findAllByUserId(long id);
}
