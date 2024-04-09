package ApiTori.Photo.DTO;

import ApiTori.Photo.Photo;
import ApiTori.User.DTO.PhotoUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhotoDTO {
    private String uniqIdentifier;
    private PhotoUserDto sender;
    private Timestamp datePhoto;
    private String url;
    private List<PhotoUserDto> recipients;


    public static PhotoDTO from(Photo photo) {
        return PhotoDTO.builder()
                .datePhoto(photo.getDatePhoto())
                .url(photo.getUrl())
                .uniqIdentifier(photo.getUniqIdentifier())
                .sender(PhotoUserDto.from(photo.getSender()))
                .recipients(PhotoUserDto.from(photo.getRecipients()))
                .build();
    }

    public static List<PhotoDTO> from(List<Photo> photos) {
        return photos.stream().map(PhotoDTO::from).collect(Collectors.toList());
    }
}
