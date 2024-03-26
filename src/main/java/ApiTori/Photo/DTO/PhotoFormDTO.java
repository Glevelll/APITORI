package ApiTori.Photo.DTO;

import jakarta.mail.Multipart;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PhotoFormDTO {
    private byte[] image;
    private String sender;
    private Timestamp datePhoto;
    private List<String> recipients;
}
