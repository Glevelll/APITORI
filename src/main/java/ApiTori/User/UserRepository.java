package ApiTori.User;

import ApiTori.User.Entityes.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Bulat Sharapov
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);
    Set<User> findAllByLoginStartingWith(String prefix);
    Set<User> findAllByLoginContaining(String substring);
    Optional<User> findById(Integer id);

    boolean existsUserByLogin(String login);

    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByLoginIn(List<String> logins);

    @Query("SELECT u.friends FROM User u WHERE u.id = :userId")
    Set<User> findFriendsByUserId(@Param("userId") Integer userId);
}
