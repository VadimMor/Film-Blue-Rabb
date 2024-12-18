package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.dto.request.LoginClientRequest;
import com.film.blue_rabb.dto.request.RegistrationUserRequest;
import com.film.blue_rabb.dto.response.*;
import com.film.blue_rabb.enums.RoleEnum;
import com.film.blue_rabb.enums.StatusEnum;
import com.film.blue_rabb.exception.custom.*;
import com.film.blue_rabb.model.Content;
import com.film.blue_rabb.utils.ActiveCode;
import com.film.blue_rabb.utils.ConvertUtils;
import com.film.blue_rabb.utils.PasswordEncoder;
import com.film.blue_rabb.utils.TokenUtils;
import com.film.blue_rabb.model.Users;
import com.film.blue_rabb.exception.ErrorMessage;
import com.film.blue_rabb.repository.UserRepository;
import com.film.blue_rabb.service.*;
import com.film.blue_rabb.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final MailService mailService;
    private final UserRepository userRepository;

    private final TokenUtils tokenUtils;
    private final ActiveCode activeCode;
    private final PasswordEncoder passwordEncoder;
    private final ConvertUtils convertUtils;

    /**
     * Метод получения токена авторизации
     * @param loginRequest форма аутентификации
     * @return токен авторизации
     */
    @Override
    @Transactional
    public AuthResponse postAuthClient(LoginClientRequest loginRequest) {
        log.trace("UserServiceImpl.getAuthToken - loginRequest {}", loginRequest);

        // Поиск пользователя
        Users user = userRepository.findFirstByEmailOrLogin(loginRequest.login(), loginRequest.login());

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
        if (!passwordEncoder.arePasswordsEquals(loginRequest.password(), user.getPassword())) {
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
                tokenUtils.generateToken(userDetails)
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

    /**
     * Получение пользователя по токену
     * @param token токен авторизации
     * @return возвращает авторизированного пользователя
     */
    @Override
    public Users getUserByToken(String token) throws EntityNotFoundException {
        log.trace("UserServiceImpl.getUserByToken - token {}", token);

        if (token == null) {
            return null;
        }

        String email = tokenUtils.getLoginFromToken(
                ConvertUtils.getStringToken(token)
        );
        Users user = userRepository.findFirstByEmail(email);

        if (user == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("JWT error", "Authorization token error"));
            throw new AuthenticationUserException(errorMessages);
        }

        return user;
    }

    /**
     * Метод добавления/удаления избранного
     * @param content контент киноискусства
     * @param token токен авторизации
     */
    @Override
    public ChangingFavoriteResponse putFavorite(Content content, String token) {
        log.trace("UserServiceImpl.putFavorite - content {}, token {}", content.getId(), token);
        String email = tokenUtils.getLoginFromToken(
                ConvertUtils.getStringToken(token)
        );
        Users user = userRepository.findFirstByEmail(email);

        if (user.getContentSet().contains(content)) {
            user.getContentSet().remove(content);
            userRepository.save(user);
            return new ChangingFavoriteResponse(false);
        }

        user.addContent(content);
        userRepository.save(user);
        return new ChangingFavoriteResponse(true);
    }

    /**
     * Метод получения избранного у пользователя
     * @param token токен авторизации
     * @return массив информации контента киноискусства
     */
    @Override
    public MassiveContentResponse getFavorite(String token) {
        log.trace("UserServiceImpl.getFavorite - token {}", token);
        String email = tokenUtils.getLoginFromToken(
                ConvertUtils.getStringToken(token)
        );
        Users user = userRepository.findFirstByEmail(email);

        List<PublicContentResponse> publicContentResponseList = user.getContentSet()
                .stream()
                .map(content -> new PublicContentResponse(
                        content.getNameRus(),
                        content.getNameEng(),
                        content.getDescription(),
                        content.getSymbolicName(),
                        content.getImageSet().iterator().next(),
                        user.getContentSet().contains(content),
                        convertUtils.formatDurationWithCases(content.getAverageDuration()),
                        (int) content.getAge()
                ))
                .toList();

        return new MassiveContentResponse(publicContentResponseList.toArray(PublicContentResponse[]::new));
    }

    @Override
    public void usersVerification() {
        log.trace("UserServiceImpl.usersVerification");

        try {
            List<Users> users = userRepository.findAllByCreateDate(
                    OffsetDateTime.now().minusHours(1).withNano(0)
            );

            if (!users.isEmpty()) {
                log.trace("Users {} delete", users.size());
                userRepository.deleteAll(users);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error delete users");
        }
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
