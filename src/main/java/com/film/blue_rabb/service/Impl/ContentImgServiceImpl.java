package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.model.ContentImg;
import com.film.blue_rabb.repository.ContentImgRepository;
import com.film.blue_rabb.service.ContentImgService;
import com.film.blue_rabb.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

        ContentImg contentImg = contentImgRepository.findFirstByImgAndContentType(file.getBytes(), file.getContentType());

        if (contentImg != null) {
            return contentImg.getId();
        }

        contentImg = new ContentImg(
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
    public boolean checkContentImg(MultipartFile file, Set<String> idImgs) throws IOException {
        log.trace("ContentImgServiceImpl.saveContentImg - file {}, idImgs {}", file.getOriginalFilename(), idImgs);

        fileUtils.checkTypeImage(file.getBytes(), file.getContentType());

        ContentImg contentImg = contentImgRepository.findFirstByImgAndContentType(file.getBytes(), file.getContentType());

        if (contentImg != null && idImgs.contains(contentImg.getId())) {
            return true;
        }

        return false;
    }

    @Override
    public void deleteSavedImages(List<String> savedImages) {
        try {
            contentImgRepository.deleteAllById(savedImages);
            log.info("Deleted {} images from the database", savedImages.size());
        } catch (Exception e) {
            log.error("Failed to delete images. Error: {}", e.getMessage());
            throw new RuntimeException("Error delete saved images");
        }
    }
}
