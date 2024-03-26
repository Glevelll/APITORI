package ApiTori.User.DTO;

import ApiTori.User.Entityes.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoUserDto {
    private String login;
    private String email;

    public static PhotoUserDto from(User user){
        return PhotoUserDto.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .build();
    }

    public static List<PhotoUserDto> from(List<User> users){
        return users.stream().map(PhotoUserDto::from).collect(Collectors.toList());
    }
}
