package ApiTori.Chat;

import ApiTori.Photo.Photo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author Bulat Sharapov and Gleb Nikishin
 */
@Table(name = "chat")
@Getter
@Setter
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat")
    private Integer id;
    @Column(name = "login_from")
    private String loginFrom;
    @Column(name = "message")
    private String message;
    @Column(name = "date_chat")
    private Timestamp dateChat;
    @ManyToOne()
    @JoinColumn(name = "id_image")
    private Photo imageId;
}
