package ApiTori.User.DTO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Bulat Sharapov
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserDataDTO {
    Integer id;
    String login;
    String password;
    String email;
}