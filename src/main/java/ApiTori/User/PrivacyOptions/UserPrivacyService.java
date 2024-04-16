package ApiTori.User.PrivacyOptions;

import ApiTori.User.DTO.CheckEmailAndCodeDTO;
import ApiTori.User.DTO.ForgotPasswordDTO;
import ApiTori.User.DTO.UpdateUserDataDTO;
import ApiTori.User.DTO.RegisterUserRequestDto;
import ApiTori.User.Entityes.User;
import ApiTori.User.MailSender.MailSender;
import ApiTori.User.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Bulat Sharapov and Gleb Nikishin
 */
@Service
@AllArgsConstructor
@ComponentScan("ToriSecurity")
public class UserPrivacyService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserPrivacyService.class);
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<?> createUser(RegisterUserRequestDto dto) {
        logger.info("Создаем юзера");

        if (mailSender.dictionaryForEmails.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (userRepository.existsUserByLogin(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Login already exists in the database.");
        }

        if (mailSender.dictionaryForEmails.get(dto.getEmail()).equals(dto.getVerificationPassword())) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());

            User createdUser = User.builder()
                    .login(dto.getLogin())
                    .password(encodedPassword)
                    .email(dto.getEmail())
                    .build();

            try {
                userRepository.save(createdUser);
                mailSender.dictionaryForEmails.remove(dto.getEmail());
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String> sendCodeToEmailReg(String email) {
        logger.info("Отправляем пароль на почту");

        if (userRepository.existsUserByEmail(email)) {
            throw new RuntimeException("Пользователь с таким email уже существует в базе данных");
        }

        mailSender.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<User> allowInTori(String login, String enteredPassword) {
        logger.info("Проверяем соответствие пароля");
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User with login not found: " + login));

        if (passwordEncoder.matches(enteredPassword, user.getPassword())) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Transactional
    public ResponseEntity<?> updateUser(int id, UpdateUserDataDTO updateData) {
        logger.info("Обновляем данные пользователя");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (updateData.getLogin() != null) {
            user.setLogin(updateData.getLogin());
        }

        if (updateData.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(updateData.getPassword());
            user.setPassword(encodedPassword);
        }

        if (updateData.getEmail() != null) {
            user.setEmail(updateData.getEmail());
        }

        try {
            userRepository.save(user);
            return ResponseEntity.ok("User data updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<?> checkPasswordForUser(int id, String password) {
        logger.info("Проверяем соответствие пароля пользователю с id: " + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.ok("Password matches.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> checkEmailAndCode(CheckEmailAndCodeDTO check) {
        logger.info("Сценарий забытого пароля - проверка email и кода");
        if (!userRepository.existsUserByEmail(check.getEmail())
                || !mailSender.dictionaryForEmails.containsKey(check.getEmail())
                || userRepository.findByEmail(check.getEmail()).isEmpty()
                || !mailSender.dictionaryForEmails.get(check.getEmail()).equals(check.getInputCode())) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    public ResponseEntity<?> forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        logger.info("Сценарий забытого пароля - обновление пароля");

        User user = userRepository.findByEmail(forgotPasswordDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + forgotPasswordDTO.getEmail()));

        user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getNewPassword()));
        mailSender.dictionaryForEmails.remove(forgotPasswordDTO.getEmail());
        userRepository.save(user);

        return ResponseEntity.ok("User password updated successfully");
    }

    public ResponseEntity<?> isLoginAvailable(String login) {
        if (userRepository.existsUserByLogin(login.toLowerCase())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Login already exists in the database.");
        }
        return ResponseEntity.ok("Login is Available");
    }

    public ResponseEntity<?> sendCodeToEmail(String email) {
        logger.info("Отправляем пароль на почту");

        if (userRepository.existsUserByEmail(email)) {
            mailSender.sendVerificationCode(email);
            return ResponseEntity.ok().build();
        } else {
            throw new RuntimeException("Пользователь не найден");
        }
    }
}
