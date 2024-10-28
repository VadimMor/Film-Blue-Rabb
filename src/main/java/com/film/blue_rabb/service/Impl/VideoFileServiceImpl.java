package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.model.VideoFile;
import com.film.blue_rabb.repository.VideoFileRepository;
import com.film.blue_rabb.service.VideoFileService;
import com.film.blue_rabb.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoFileServiceImpl implements VideoFileService {
    private final VideoFileRepository videoFileRepository;

    private final FileUtils fileUtils;

    /**
     * Метод сохранения файла в бд
     * @param file файл видео
     * @return id файла
     */
    @Override
    public String saveVideoFile(MultipartFile file) throws IOException {
        log.trace("VideoFileServiceImpl.saveVideoFile - file {}", file.getOriginalFilename());

        fileUtils.checkTypeVideo(file.getBytes(), file.getContentType());

        VideoFile videoFile = videoFileRepository.findFirstByVideoAndContentType(file.getBytes(), file.getContentType());

        if (videoFile != null) {
            return videoFile.getId();
        }

        videoFile = new VideoFile(
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType(),
                file.getSize(),
                new Date(),
                new Date()
        );
        videoFileRepository.save(videoFile);

        return videoFile.getId();
    }

    /**
     * Удаление сохраненного видео файла
     * @param videoFile видео файл
     * @throws RuntimeException обработка ошибок при удалении
     */
    @Override
    public void deleteVideoFile(String videoFile) {
        log.trace("VideoFileServiceImpl.deleteVideoFile - videoFile {}", videoFile);

        try {
            videoFileRepository.deleteById(UUID.fromString(videoFile));
            log.info("Deleted {} video from the database", videoFile);
        } catch (Exception e) {
            log.error("Failed to delete video. Error: {}", e.getMessage());
            throw new RuntimeException("Error delete saved video");
        }
    }
}
