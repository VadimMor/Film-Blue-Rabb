package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.model.ContentImg;
import com.film.blue_rabb.repository.ContentImgRepository;
import com.film.blue_rabb.service.ContentImgService;
import com.film.blue_rabb.utils.FileUtils;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentImgServiceImpl implements ContentImgService {
    @Autowired
    private final ContentImgRepository contentImgRepository;

    private final FileUtils fileUtils;

    /**
     * Сохранение изображений контента в базу данных в MongoDB
     * @param file изображение
     */
    @Override
    public String saveContentImg(MultipartFile file) throws IOException {
        log.trace("ContentImgServiceImpl.saveContentImg - file {}", file.getOriginalFilename());

        fileUtils.checkTypeImage(file.getBytes(), file.getContentType());

        ContentImg contentImg = new ContentImg(
                file.getOriginalFilename(),
                file.getBytes(),
                file.getContentType(),
                file.getSize(),
                new Date(),
                new Date()
        );
        contentImgRepository.save(contentImg);

        return contentImg.getId();
    }

    @Override
    public void deleteSavedImages(List<String> savedImages) {
        try {
            contentImgRepository.deleteAllById(savedImages);
            log.info("Deleted {} images from the database", savedImages.size());
        } catch (Exception e) {
            log.error("Failed to delete images. Error: {}", e.getMessage());
        }
    }
}
