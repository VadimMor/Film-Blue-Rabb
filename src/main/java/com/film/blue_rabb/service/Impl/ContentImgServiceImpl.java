package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.dto.response.PublicImage;
import com.film.blue_rabb.model.ContentImg;
import com.film.blue_rabb.repository.ContentImgRepository;
import com.film.blue_rabb.service.ContentImgService;
import com.film.blue_rabb.utils.FileUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    /**
     * Метод удаления изображений, сохраненных в бд
     * @param savedImages список id сохраненных изображений
     * @throws RuntimeException обработка ошибок при удалении
     */
    @Override
    public void deleteSavedImages(List<String> savedImages) {
        log.trace("ContentImgServiceImpl.deleteSavedImages - savedImages {}", savedImages.size());
        try {
            contentImgRepository.deleteAllById(savedImages);
            log.info("Deleted {} images from the database", savedImages.size());
        } catch (Exception e) {
            log.error("Failed to delete images. Error: {}", e.getMessage());
            throw new RuntimeException("Error delete saved images");
        }
    }

    /**
     * Метод получения данных для вывода изображения
     * @param name id изображения
     * @return данные для вывода
     */
    @Override
    public PublicImage getImage(String name) throws EntityNotFoundException {
        log.trace("ContentImgServiceImpl.getImage - name {}", name);

        try {
            Optional<ContentImg> contentImg = contentImgRepository.findById(name);

            if (contentImg == null) {
                throw new EntityNotFoundException("Image not found");
            }

            return new PublicImage(
                    contentImg.get().getImg(),
                    contentImg.get().getName(),
                    contentImg.get().getContentType()
            );
        } catch (RuntimeException e) {
            log.error("Failed to get image. Error: {}", e.getMessage());
            throw new RuntimeException("Error get image");
        }
    }
}
