package ApiTori.Photo;

import ApiTori.Chat.ChatRepository;
import ApiTori.Photo.DTO.PhotoDTO;
import ApiTori.User.Entityes.User;
import ApiTori.User.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.websocket.OnMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Component
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;

    @OnMessage
    @Override
    public PhotoDTO sendPhoto(String loginOfSender, List<String> loginsOfRecipients, byte[] image, Timestamp datePhoto) {
        String fileName = generateUniqueFileName();
        String imageUrl = saveImageAndGetUrl(image, fileName);

        List<User> recipientsUsersList = userRepository.findByLoginIn(loginsOfRecipients);
        User senderUser;

        Optional<User> optionalUser = userRepository.findByLogin(loginOfSender);
        if (optionalUser.isPresent()) {
            senderUser = optionalUser.get();
            recipientsUsersList.add(senderUser);
        } else {
            throw new IllegalArgumentException("Sender not found");
        }

        Photo photo = Photo.builder()
                .sender(senderUser)
                .recipients(recipientsUsersList)
                .datePhoto(datePhoto)
                .url(imageUrl)
                .build();

        photoRepository.save(photo);

        return PhotoDTO.from(photo);
    }


    private String generateUniqueFileName() {
        return UUID.randomUUID().toString();
    }

    private String saveImageAndGetUrl(byte[] image, String fileName) {
        String folderPath = "C:/Users/Глеб/Glevel/image";

        try (FileOutputStream fos = new FileOutputStream(folderPath + "/" + fileName + ".jpg")) {
            fos.write(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "http://localhost:8081/images/" + fileName + ".jpg";
    }

    @Override
    public PhotoDTO getPhoto(Integer identifier) {

        Optional<Photo> optionalPhoto = photoRepository.findByUniqIdentifier(identifier);

        Photo photo;
        if (optionalPhoto.isPresent()) {
            photo = optionalPhoto.get();
        } else {
            return null;
        }
        return PhotoDTO.from(photo);
    }

    @Transactional
    @Override
    public boolean deletePhoto(Integer userId, Integer photoId) {
        Optional<Photo> optionalPhoto = photoRepository.findByUniqIdentifier(photoId);

        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();
            if (photo.getSender().getId().equals(userId)) {
                photoRepository.delete(photo);
            } else {
                List<User> recipients = photo.getRecipients();
                recipients.removeIf(user -> user.getId().equals(userId));
                photo.setRecipients(recipients);
                photoRepository.save(photo);
            }
            return true;
        }
        return false;
    }


    @Override
    public List<PhotoDTO> getRecievedPhotos(String loginOfUser) {
        if (userRepository.existsUserByLogin(loginOfUser)) {
            List<Photo> receivedPhotos = photoRepository.findPhotosByRecipientLogin(loginOfUser);
            if (!receivedPhotos.isEmpty()) {
                System.out.println("Received photos: ");
                List<PhotoDTO> photoDTOs = PhotoDTO.from(receivedPhotos);
                photoDTOs.forEach(photoDTO -> System.out.println(photoDTO.getUrl()));
                return photoDTOs;
            }
        }
        return null;
    }

}
