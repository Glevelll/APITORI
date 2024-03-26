package ApiTori.Chat.DTO;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChatDTORequest {
    private Integer id;
    private String loginFrom;
    private String message;
    private Timestamp dateChat;
    private int imageId;
}
