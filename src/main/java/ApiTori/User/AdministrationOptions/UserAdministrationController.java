package ApiTori.User.AdministrationOptions;

import ApiTori.User.Entityes.User;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author Bulat Sharapov and Gleb Nikishin
 */

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserAdministrationController {
    private UserService userService;

    @GetMapping("getUserByLogin/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        return userService.getUserByLogin(login);
    }

    /**
     * @param id    айди человека, который отправляет заявку на добавление в друзья
     * @param login логин человека, которому он отправляет эту заявку
     */
    @PostMapping(value = "{id}/sendFriendRequest/{login}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> sendFriendRequest(@PathVariable Integer id, @PathVariable String login) {
        return userService.sendFriendRequest(id, login);
    }

    /**
     * @param id    айди человека, которому отправили заявку
     * @param login логин человека, который отправил заявку
     */
    @PostMapping("{id}/acceptFriend/{login}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Integer id, @PathVariable String login) {
        return userService.acceptFriendRequest(id, login);
    }

    /**
     * Метод будет отклонять заявку на добавляение в кореша
     *
     * @param id    айди человека, которому отправили заявку
     * @param login логин человека, который отправил заявку
     */
    @PostMapping("{id}/rejectFriend/{login}")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable Integer id, @PathVariable String login) {
        return userService.rejectFriendRequest(id, login);
    }

    /**
     * Метод будет удалять заявку на добавление в кореша
     *
     * @param id    айди человека, который удаляет друга
     * @param login айди друга (бывшего) типа марухи
     */
    @PostMapping("{id}/deleteFriend/{login}")
    public ResponseEntity<?> deleteFriend(@PathVariable Integer id, @PathVariable String login) {
        return userService.deleteFriend(id, login);
    }

    /**
     * @param id человека, чьи заявки мы смотрим
     * @return
     */
    @GetMapping("getIncomingRequests/{id}")
    public ResponseEntity<?> getIncomingRequests(@PathVariable Integer id) {
        return userService.getIncomingRequests(id);
    }

    /**
     * @param id человека, чьи заявки мы смотрим
     * @return возвращаем нескока рандомных типов
     */
    @GetMapping("getRandomPips/{id}")
    public ResponseEntity<?> getRandomPips(@PathVariable Integer id) {
        return userService.getRandomPips(id);
    }

    /**
     * @param id человека, чьи заявки мы смотрим
     * @return возвращаем нескока рандомных типов
     */
    @GetMapping("getFriends/{id}")
    public ResponseEntity<?> getFriends(@PathVariable Integer id) {
        return userService.getFriends(id);
    }

    @GetMapping("findUsers/{searchBarData}")
    public CompletableFuture<ResponseEntity<?>> findUsers(@PathVariable String searchBarData) {
        return userService.findUsers( searchBarData);
    }
}
