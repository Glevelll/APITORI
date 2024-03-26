package ApiTori.Chat;

import ApiTori.Photo.Photo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Bulat Sharapov and Gleb Nikishin
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Chat c WHERE c.imageId = :photo")
    void deleteByImageId(@Param("photo") Photo photo);

    @Query("SELECT c FROM Chat c WHERE c.imageId = :image")
    List<Chat> findByImageId(@Param("image") Photo image);

}
