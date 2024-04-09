package ApiTori.Chat.DTO;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChatDTO {
    private String message;
    private Timestamp dateChat;
    private String imageId;
}
