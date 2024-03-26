package ApiTori.User.AdministrationOptions;

import ApiTori.Photo.Photo;
import ApiTori.Photo.PhotoRepository;
import ApiTori.User.Entityes.User;
import ApiTori.User.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Bulat Sharapov and Gleb Nikishin
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    public ResponseEntity<User> getUserByLogin(String login) {
        System.out.println("Ищем по логину");
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("We dont have this login in database: " + login));
        return ResponseEntity.ok().body(user);
    }

    @Transactional
    public ResponseEntity<String> sendFriendRequest(Integer id, String login) {
        System.out.println("Отправляем заявку в друзья");
        User userSender = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        User userRequester = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + login));

        userRequester.getFriendsRequests().add(userSender);
        userRepository.save(userRequester);

        return ResponseEntity.ok("Заявку создали");
    }

    @Transactional
    public ResponseEntity<?> acceptFriendRequest(Integer id, String login) {
        System.out.println("Принимаем заявку в друзья");
        User userAccepter = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        User userRequester = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + login));

        if (!userAccepter.getFriendsRequests().contains(userRequester)) {
            return ResponseEntity.notFound().build();
        }

        userAccepter.getFriends().add(userRequester);
        userRequester.getFriends().add(userAccepter);
        userAccepter.getFriendsRequests().remove(userRequester);

        userRepository.save(userAccepter);

        return ResponseEntity.ok("Добавили вашего друга !");
    }

    @Transactional
    public ResponseEntity<?> rejectFriendRequest(Integer id, String login) {
        System.out.println("Отклоняем заявку в друзья");
        User userWhoRejectFriend = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        User userWhoRejected = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + login));

        if (!userWhoRejectFriend.getFriendsRequests().contains(userWhoRejected)) {
            return ResponseEntity.notFound().build();
        }

        userWhoRejectFriend.getFriendsRequests().remove(userWhoRejected);
        userRepository.save(userWhoRejectFriend);

        return ResponseEntity.ok("Друга Отклонили");
    }

    @Transactional
    public ResponseEntity<?> deleteFriend(Integer id, String login) {
        System.out.println("Удаляем из друзей");
        User userWhoDeleteFriend = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        User userWhoDeleted = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + login));

        if (userWhoDeleteFriend.equals(userWhoDeleted)) {
            return ResponseEntity.badRequest().body("Это вообще то ты сам, другалечек");
        }

        userWhoDeleteFriend.getFriends().remove(userWhoDeleted);
        userWhoDeleted.getFriends().remove(userWhoDeleteFriend);

        List<Photo> deletedUserPhotos = photoRepository.findPhotosBySenderId(userWhoDeleteFriend.getId());
        deletedUserPhotos.forEach(photo -> photo.getRecipients().remove(userWhoDeleted));

        userRepository.save(userWhoDeleteFriend);
        deletedUserPhotos.forEach(photoRepository::save);

        return ResponseEntity.ok("Корешок (Маруха) удален");
    }

    @Transactional
    public ResponseEntity<?> getIncomingRequests(Integer id) {
        User userWhoHaveRequests = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        Set<User> incomingFriendsSet = userWhoHaveRequests.getFriendsRequests();
        List<String> usersList = incomingFriendsSet.stream()
                .map(User::getLogin)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usersList);
    }

    @Transactional
    public ResponseEntity<?> getRandomPips(Integer id) {
        User userWhoNeedPips = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        Set<User> userFriends = userWhoNeedPips.getFriends();
        Set<User> userRequests = userWhoNeedPips.getFriendsRequests();

        List<String> userArrayList = new ArrayList<>();

        while (userArrayList.size() <= 10) {
            User randomUser = getRandomUser();

            if (!randomUser.equals(userWhoNeedPips) &&
                    !userFriends.contains(randomUser) &&
                    !userRequests.contains(randomUser) &&
                    !userArrayList.contains(randomUser.getLogin())) {
                userArrayList.add(randomUser.getLogin());
            }
        }
        return ResponseEntity.ok(userArrayList);
    }

    @Transactional
    public User getRandomUser() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            throw new RuntimeException("База данных пользователей пуста");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(allUsers.size());
        return allUsers.get(randomIndex);
    }

    @Transactional
    public ResponseEntity<?> getFriends(Integer userId) {
        System.out.println("Получаем корешей");
        Set<User> userSet = userRepository.findFriendsByUserId(userId);
        List<String> usersList = userSet.stream()
                .map(User::getLogin)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usersList);
    }

    @Async
    @Transactional
    public CompletableFuture<ResponseEntity<?>> findUsers(String searchBarData) {
        List<String> usersLogins;
        if (searchBarData.length() == 1) {
            usersLogins = userRepository.findAllByLoginStartingWith(searchBarData)
                    .stream()
                    .map(User::getLogin)
                    .toList();
        } else {
            usersLogins = userRepository.findAllByLoginContaining(searchBarData)
                    .stream()
                    .map(User::getLogin)
                    .toList();
        }
        if (usersLogins.isEmpty()) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        return CompletableFuture.completedFuture(ResponseEntity.ok(usersLogins));
    }
}