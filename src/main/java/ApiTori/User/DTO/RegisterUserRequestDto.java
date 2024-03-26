package ApiTori.User.DTO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


/**
 * @author Bulat Sharapov
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserRequestDto {
    String login;
    String password;
    String email;
    String verificationPassword;
}
