package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.dto.request.RegistrationUserRequest;
import com.film.blue_rabb.dto.response.RegistrationUserResponse;
import com.film.blue_rabb.enums.RoleEnum;
import com.film.blue_rabb.enums.StatusEnum;
import com.film.blue_rabb.exception.custom.*;
import com.film.blue_rabb.utils.ActiveCode;
import com.film.blue_rabb.utils.JwtTokenUtils;
import com.film.blue_rabb.dto.request.AuthForm;
import com.film.blue_rabb.dto.response.AuthResponse;
import com.film.blue_rabb.model.Users;
import com.film.blue_rabb.exception.ErrorMessage;
import com.film.blue_rabb.repository.UserRepository;
import com.film.blue_rabb.service.*;
import com.film.blue_rabb.service.UserService;
import com.film.blue_rabb.utils.PasswordEncoder;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final MailService mailService;

    private final UserRepository userRepository;

    private final JwtTokenUtils jwtTokenUtils;
    private final ActiveCode activeCode;
    private final PasswordEncoder passwordEncoder;

    /**
     * Метод получения токена авторизации
     * @param authForm форма аутентификации
     * @return токен авторизации
     */
    @Override
    @Transactional
    public AuthResponse getAuthToken(AuthForm authForm) throws AuthenticationUserException, BannedException {
        log.trace("UserServiceImpl.getAuthToken - authForm {}", authForm);

        // Поиск пользователя
        Users user = userRepository.findFirstByEmailOrLogin(authForm.login(), authForm.login());

        // Проверка на существование аккаунта
        if (
                user == null ||
                user.getStatus().equals(StatusEnum.INACTIVE)
        ) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Authentication error", "The email/login or password is incorrect!"));
            throw new AuthenticationUserException(errorMessages);
        }

        // Проверка на бан аккаунта
        checkBanUser(user);

        // Проверка веденного пароля
        if (!passwordEncoder.arePasswordsEquals(authForm.password(), user.getPassword())) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Authentication error", "The email/login or password is incorrect!"));
            throw new AuthenticationUserException(errorMessages);
        }

        // Детали пользователя для токена
        UserDetails userDetails = new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );

        // Создание JSON web token и его возвращение
        return new AuthResponse(
                jwtTokenUtils.generateToken(userDetails)
        );
    }

    /**
     * Метод создания пользователя и сохранение в бд
     * @param registrationUserRequest данные для регистрации
     * @return данные о успешной регистрации
     */
    @Override
    public RegistrationUserResponse createUser(RegistrationUserRequest registrationUserRequest) throws UserAlreadyCreatedException, MessagingException {
        log.trace("UserServiceImpl.createUser - registrationUserRequest {}", registrationUserRequest);
        // Ищем пользователя в базе данных по email и логину
        Users user = userRepository.findFirstByEmailOrLogin(registrationUserRequest.email(), registrationUserRequest.login());

        // Если пользователь уже существует, выбрасываем исключение UserAlreadyCreatedException
        if (user != null) {
            throw new UserAlreadyCreatedException();
        }

        // Создаем нового пользователя с данными из запроса
        Users newUser = new Users(
                registrationUserRequest.login(),
                registrationUserRequest.email(),
                passwordEncoder.getEncryptedPassword(registrationUserRequest.password()),
                registrationUserRequest.birthday(),
                RoleEnum.CLIENT,
                StatusEnum.INACTIVE,
                activeCode.generateCode(),
                null,
                OffsetDateTime.now()
        );

        // Отправляем письмо с кодом активации на email нового пользователя
        mailService.sendActiveCode(
                newUser.getEmail(),
                newUser.getActiveCode()
        );

        // Сохраняем нового пользователя в базе данных и обновляем состояние
        userRepository.saveAndFlush(newUser);

        // Возвращаем ответ с логином и email нового пользователя
        return new RegistrationUserResponse(
                newUser.getLogin(),
                newUser.getEmail()
        );
    }

    /**
     * Метод активации аккаунта
     * @param code код активации
     * @param email почта для поиска аккаунта
     * @return данные о успешной регистрации
     */
    @Override
    public RegistrationUserResponse activeUser(String code, String email) throws InvalidDataException {
        log.trace("UserServiceImpl.activeUser - code {}, email {}", code, email);
        // Поиск пользователя по почте
        Users user = userRepository.findFirstByEmail(email);

        // Выбрасывания исключений
        if (user == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Activation error", "The email or code incorrect!"));
            throw new InvalidDataException(errorMessages);
        }

        // Проверка на бан аккаунта
        checkBanUser(user);

        // Проверка на активного аккаунта
        if (!user.getStatus().equals(StatusEnum.INACTIVE)) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Activation error", "The account has already been activated!"));
            throw new InvalidDataException(errorMessages);
        }
        // Проверка кода активации
        else if (!Objects.equals(user.getActiveCode(), code)) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Activation error", "The email or code incorrect!"));
            throw new InvalidDataException(errorMessages);
        }

        // Изменение статуса аккаунта
        user.setStatus(StatusEnum.ACTIVE);

        // Сохранение изменений аккаунта
        userRepository.save(user);

        // Возврат ответа с логином и email активированный аккаунт
        return new RegistrationUserResponse(
                user.getLogin(),
                user.getEmail()
        );
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, AuthenticationUserException {
        log.trace("UserServiceImpl.loadUserByUsername - email {}", email);

        Users user = userRepository.findFirstByEmailOrLogin(email, email);

        if (user == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Authentication error", "The email/login or password is incorrect!"));
            throw new AuthenticationUserException(errorMessages);
        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );
    }

    private void checkBanUser(Users user) throws BannedException {
        log.trace("UserServiceImpl.checkBanUser - user_id {}", user.getId());

        // Проверка на бан аккаунта
        if (user.getStatus().equals(StatusEnum.BANNED) && user.getBannedTime() != null) {
            throw  new BannedException(true, user.getBannedTime());
        } else if (user.getStatus().equals(StatusEnum.BANNED)) {
            throw  new BannedException(true);
        }
    }
}
