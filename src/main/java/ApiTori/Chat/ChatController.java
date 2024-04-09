package ApiTori.Chat;

import ApiTori.Chat.DTO.ChatDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/chat")
public class ChatController {
    private ChatService chatService;

    @PostMapping("/{loginFrom}/sendMessage")
    public ResponseEntity<?> sendMessage(@PathVariable String loginFrom, @RequestBody ChatDTO request) {
        return chatService.sendMessage(loginFrom, request.getMessage(), request.getDateChat(), request.getImageId());
    }

    @GetMapping("/getMessages")
    public ResponseEntity<?> getMessages(@RequestParam List<String> imageIds) {
        return chatService.getMessages(imageIds);
    }

    @PostMapping("/{loginFrom}/deleteMessage/{id_chat}")
    public ResponseEntity<?> deleteMessage(@PathVariable String loginFrom, @PathVariable int id_chat) {
        return chatService.deleteMessage(loginFrom, id_chat);
    }


}
