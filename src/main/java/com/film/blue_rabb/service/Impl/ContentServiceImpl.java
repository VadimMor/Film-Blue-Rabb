package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.dto.request.AddContentRequest;
import com.film.blue_rabb.dto.response.AddContentResponse;
import com.film.blue_rabb.exception.ErrorMessage;
import com.film.blue_rabb.exception.custom.InvalidDataException;
import com.film.blue_rabb.model.Content;
import com.film.blue_rabb.repository.ContentRepository;
import com.film.blue_rabb.service.ContentImgService;
import com.film.blue_rabb.service.ContentService;
import com.film.blue_rabb.service.UserService;
import com.film.blue_rabb.utils.ConvertUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final UserService userService;
    private final ContentImgService contentImgService;

    private final ContentRepository contentRepository;

    private final ConvertUtils convertUtils;

    /**
     * Метод добавления контента киноиндустрии
     * @param addContentRequest информацияи о контенте
     * @param files изображения контента
     * @return сообщение об успешном создании контента
     */
    @Override
    public AddContentResponse addContent(AddContentRequest addContentRequest, MultipartFile[] files) throws IOException, EntityExistsException {
        log.trace("ContentServiceImpl.addContent - addContentRequest {}, count files {}", addContentRequest, files.length);

        if (contentRepository.findByNameRusOrNameEng(addContentRequest.fullNameRus(), addContentRequest.fullName()) != null) {
            throw new EntityExistsException("Media content already exists!");
        }

        List<String> savedImages = new ArrayList<>();

        try {
            Content content = new Content(
                    addContentRequest.fullNameRus(),
                    addContentRequest.fullName(),
                    convertUtils.formatName(addContentRequest.fullName()),
                    addContentRequest.description(),
                    addContentRequest.age(),
                    addContentRequest.creator(),
                    addContentRequest.durationMinutes(),
                    null
            );

            for (MultipartFile file : files) {
                String savedImage = contentImgService.saveContentImg(file);
                savedImages.add(savedImage);
                content.addImage(savedImage);
            }

            contentRepository.saveAndFlush(content);


            return new AddContentResponse(
                    content.getNameRus(),
                    "Media content has been successfully saved in the database"
            );
        } catch (IOException e) {
            log.error("IOException occurred while saving content: {}", e.getMessage());
            contentImgService.deleteSavedImages(savedImages);
            throw new IOException("Failed to save content due to IO error.", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while adding content.", e);
        }
    }

    /**
     * Метод обновления информации контента киноискусства
     * @param addContentRequest информацияи о контенте
     * @param files изображения контента
     * @param symbolicName символичное имя контента
     * @return сообщение об успешном обновлении контента
     */
    @Override
    public AddContentResponse updateContent(AddContentRequest addContentRequest, MultipartFile[] files, String symbolicName) throws EntityNotFoundException, IOException {
        log.trace("ContentServiceImpl.updateContent - addContentRequest {}, count files {}, symbolicName {}", addContentRequest, files.length, symbolicName);

        Content content = contentRepository.findFirstBySymbolicName(symbolicName);

        if (content == null) {
            throw new EntityNotFoundException(String.format("An entity named \"%s\" was not found", symbolicName));
        }

        List<String> savedImages = new ArrayList<>();
        Set<String> oldImages = content.getImageSet();

        try {
            content.setNameRus(addContentRequest.fullNameRus());
            content.setNameEng(addContentRequest.fullName());
            content.setSymbolicName(convertUtils.formatName(addContentRequest.fullName()));
            content.setDescription(addContentRequest.description());
            content.setAge(addContentRequest.age());
            content.setCreator(addContentRequest.creator());
            content.setAverageDuration(addContentRequest.durationMinutes());

            for (MultipartFile file : files) {
                String savedImage = contentImgService.saveContentImg(file);
                savedImages.add(savedImage);
            }

            content.setImageSet(new HashSet<>(savedImages));

            contentRepository.save(content);

            return new AddContentResponse(
                    content.getNameRus(),
                    "Media content has been successfully update in the database"
            );
        } catch (IOException e) {
            log.error("IOException occurred while saving content: {}", e.getMessage());
            content.setImageSet(new HashSet<>(oldImages));

            List<String> newImages = savedImages.stream()
                    .filter(img -> !oldImages.contains(img))
                    .collect(Collectors.toList());

            contentImgService.deleteSavedImages(newImages);
            throw new IOException("Failed to save content due to IO error.", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while updating content.", e);
        }
    }
}
