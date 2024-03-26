package ApiTori.User.DTO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckEmailAndCodeDTO {
    String email;
    String inputCode;
}
