package ApiTori.Photo;

import ApiTori.Photo.DTO.PhotoDTO;

import java.sql.Timestamp;
import java.util.List;

public interface PhotoService {

    PhotoDTO sendPhoto(String loginOfSender, List<String> loginsOfRecipients, byte[] description, Timestamp datePhoto);

    PhotoDTO getPhoto(Integer identifier);

    boolean deletePhoto(Integer identifier, Integer photoId);

    List<PhotoDTO> getRecievedPhotos(String loginOfUser);

}
