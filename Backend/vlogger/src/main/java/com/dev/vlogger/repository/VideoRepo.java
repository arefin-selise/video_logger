package com.dev.vlogger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.vlogger.model.Video;

public interface VideoRepo extends JpaRepository<Video, Long> {

}
