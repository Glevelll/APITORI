package ApiTori.Photo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author Bulat Sharapov
 */
public interface PhotoRepository extends JpaRepository<Photo, String> {
    Optional<Photo> findByUniqIdentifier(String uniqIdentifier);

    @Query("SELECT p FROM Photo p WHERE p.sender.id = :senderId")
    List<Photo> findPhotosBySenderId(@Param("senderId") Integer senderId);

    @Query("SELECT p FROM Photo p JOIN p.recipients r WHERE r.login = :userLogin")
    List<Photo> findPhotosByRecipientLogin(@Param("userLogin") String userLogin);

}