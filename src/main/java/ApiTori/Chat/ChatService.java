package ApiTori.Chat;

import ApiTori.Chat.DTO.ChatDTORequest;
import ApiTori.Photo.Photo;
import ApiTori.Photo.PhotoRepository;
import ApiTori.User.UserRepository;
import ApiTori.User.Entityes.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Bulat Sharapov and Gleb Nikishin
 */

@Service
@AllArgsConstructor
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PhotoRepository photoRepository;

    //не дает вернуть ок, если поставить тип CHAT
    public ResponseEntity<?> sendMessage(String loginFrom, String message, Timestamp dateChat, String imageId) {
        System.out.println("Отправляем сообщение");
        Optional<User> userFrom = userRepository.findByLogin(loginFrom); // человек, который отправляет
        Optional<Photo> imageTo = photoRepository.findByUniqIdentifier(imageId); // человек, которому отправляют
        Chat chat = new Chat();

        if (userFrom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (imageTo.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            chat.setLoginFrom(userFrom.get().getLogin());  // Заполнение полей сообщения
            chat.setMessage(message);
            chat.setDateChat(dateChat);
            chat.setImageId(imageTo.get());
            chatRepository.save(chat); // Сохранение сообщения в базе данных

            return ResponseEntity.ok().body("Сообщение отправлено");
        }
    }

    // ТУТ АНАЛОГИЧНАЯ ИСТОРИЯ
    public ResponseEntity<?> getMessages(List<String> imageIds) {
        System.out.println("Getting messages");
        List<Photo> photos = photoRepository.findAllById(imageIds);
        List<ChatDTORequest> chatDTOs = new ArrayList<>();

        photos.forEach(photo -> {
            List<Chat> chats = chatRepository.findByImageId(photo);
            if (!chats.isEmpty()) {
                List<ChatDTORequest> chatDTOsForPhoto = chats.stream()
                        .map(chat -> {
                            ChatDTORequest chatDTO = new ChatDTORequest();
                            chatDTO.setId(chat.getId());
                            chatDTO.setLoginFrom(chat.getLoginFrom());
                            chatDTO.setMessage(chat.getMessage());
                            chatDTO.setDateChat(chat.getDateChat());
                            chatDTO.setImageId(chat.getImageId().getUniqIdentifier());
                            return chatDTO;
                        })
                        .toList();

                chatDTOs.addAll(chatDTOsForPhoto);
            }
        });

        if (!chatDTOs.isEmpty()) {
            return ResponseEntity.ok().body(chatDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> deleteMessage(String loginFrom, int idChat) {
        Optional<User> user = userRepository.findByLogin(loginFrom);
        Optional<Chat> chatToDelete = chatRepository.findById(idChat);

        if (user.isEmpty() || chatToDelete.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Chat chat = chatToDelete.get();
        if (chat.getLoginFrom().equals(loginFrom)) {
            chatRepository.delete(chat);
            return ResponseEntity.ok().body("Сообщение удалено");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Вы не можете удалять это сообщение");
        }
    }
}
