package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.dto.request.AddVideoRequest;
import com.film.blue_rabb.dto.response.VideoResponse;
import com.film.blue_rabb.model.Video;
import com.film.blue_rabb.model.VideoFile;
import com.film.blue_rabb.repository.VideoRepository;
import com.film.blue_rabb.service.VideoFileService;
import com.film.blue_rabb.service.VideoService;
import com.film.blue_rabb.utils.ConvertUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;

    private final VideoFileService videoFileService;

    private final ConvertUtils convertUtils;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Метод добавления видео и информации о нем в бд
     * @param file файл видео
     * @param addVideoRequest информация о видео
     * @return сохраненной видео
     */
    @Override
    public Video addVideo(MultipartFile file, AddVideoRequest addVideoRequest) throws IOException {
        log.trace("VideoServiceImpl.addVideo - file {}, addVideoRequest {}", file.getOriginalFilename(), addVideoRequest);

        if (videoRepository.findFirstByFullName(addVideoRequest.fullName()) != null) {
            throw new EntityExistsException("Entity exists!");
        }

        String videoFile = null;

        try {
            videoFile = videoFileService.saveVideoFile(file);

            Video video = new Video(
                    addVideoRequest.fullName(),
                    convertUtils.formatName(addVideoRequest.fullName()),
                    addVideoRequest.description(),
                    addVideoRequest.durationMinutes(),
                    videoFile,
                    new Date(),
                    new Date()
            );

            videoRepository.saveAndFlush(video);

            return video;
        } catch (IOException e) {
            log.error("IOException occurred while saving content: {}", e.getMessage());
            throw new IOException("Failed to save content due to IO error.", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while adding content.", e);
        }
    }

    /**
     * Метод получения информации о видео
     * @param id id видео
     * @return информация о видео
     */
    @Override
    public VideoResponse getVideo(Long id) throws EntityNotFoundException {
        log.trace("VideoServiceImpl.getVideo - id {}", id);

        try {
            Video video = videoRepository.getReferenceById(id);

            return new VideoResponse(
                    video.getFullName(),
                    video.getShortName(),
                    video.getDescription(),
                    video.getDurationMinutes()
            );
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while adding content.", e);
        }
    }

    @Override
    public Optional<VideoFile> getVideoFile(Long id) throws EntityNotFoundException {
        Video video = videoRepository.getReferenceById(id);

        if (video.getIdMongo() == null) {
            throw new EntityNotFoundException("Video not found");
        }

        Optional<VideoFile> videoFile = videoFileService.getVideoFile(video.getIdMongo());

        return videoFile;
    }
}
