package ApiTori.User.PrivacyOptions;

import ApiTori.User.DTO.CheckEmailAndCodeDTO;
import ApiTori.User.DTO.UpdateUserDataDTO;
import ApiTori.User.DTO.ForgotPasswordDTO;
import ApiTori.User.DTO.RegisterUserRequestDto;
import ApiTori.User.Entityes.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Bulat Sharapov and Gleb Nikishin
 */

@RestController
@RequestMapping("/api/v1/privacy")
@RequiredArgsConstructor
public class PrivacyController {
    private final UserPrivacyService userPrivacyService;

    @PostMapping("/registerUser")
    public ResponseEntity<?> userRegistration(@RequestBody RegisterUserRequestDto dto) {
        return userPrivacyService.createUser(dto);
    }

    @PostMapping("/sendCodeToEmail")
    public ResponseEntity<?> sendCodeToEmail(@RequestParam String email) {
        return userPrivacyService.sendCodeToEmail(email);
    }

    @PostMapping("/entry")
    public ResponseEntity<User> entranceInTory(@RequestParam String login, @RequestParam String password) {
        return userPrivacyService.allowInTori(login, password);
    }

    @PostMapping("/checkEmailAndCode")
    public ResponseEntity<?> checkEmailAndCode(@RequestBody CheckEmailAndCodeDTO check) {
        return userPrivacyService.checkEmailAndCode(check);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        return userPrivacyService.forgotPassword(forgotPasswordDTO);
    }

    @GetMapping("/isLoginAvailable/{login}")
    public ResponseEntity<?> isLoginAvailable(@PathVariable String login) {
        return userPrivacyService.isLoginAvailable(login);
    }

    @PostMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody UpdateUserDataDTO updateData) {
        return userPrivacyService.updateUser(id, updateData);
    }

    @PostMapping("/checkPassword")
    public ResponseEntity<?> checkPasswordForUser(@RequestParam int id, @RequestParam String password) {
        return userPrivacyService.checkPasswordForUser(id, password);
    }

}