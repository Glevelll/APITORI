package ApiTori.Photo;

import ApiTori.Chat.ChatRepository;
import ApiTori.Photo.DTO.PhotoDTO;
import ApiTori.Photo.DTO.PhotoFormDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/photo")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @PostMapping("/send")
    public ResponseEntity<?> sendPhotoToRecipients(@RequestBody PhotoFormDTO photoFormDTO) {
        try {
            PhotoDTO savedPhoto = photoService.sendPhoto(photoFormDTO.getSender(), photoFormDTO.getRecipients(), photoFormDTO.getImage(), photoFormDTO.getDatePhoto());
            if (savedPhoto != null) {
                return ResponseEntity.ok().body(savedPhoto.getUrl());
            } else {
                return ResponseEntity.badRequest().body("Failed to save photo. Invalid data provided.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to save photo. Error: " + e.getMessage());
        }
    }

    @GetMapping("/{identifier}")
    @ResponseBody
    public ResponseEntity<PhotoDTO> getPhoto(@PathVariable Integer identifier) {
        return ResponseEntity.ok().body(photoService.getPhoto(identifier));
    }

    @PostMapping("/delete/{userId}/{photoId}")
    public ResponseEntity<?> deletePhoto(@PathVariable Integer userId, @PathVariable Integer photoId) {
        boolean deletionResult = photoService.deletePhoto(userId, photoId);
        if (deletionResult) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to delete this photo");
        }
    }

    @GetMapping("/{login_user}/recievedPhotos")
    @ResponseBody
    public ResponseEntity<?> getRecievedPhotosOfUserByLogin(@PathVariable String login_user) {
        List<PhotoDTO> receivedPhotos = photoService.getRecievedPhotos(login_user);
        if (receivedPhotos != null && !receivedPhotos.isEmpty()) {
            return ResponseEntity.ok().body(receivedPhotos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
