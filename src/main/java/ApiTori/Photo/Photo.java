package ApiTori.Photo;

import ApiTori.User.Entityes.User;
import com.sun.mail.iap.ByteArray;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Bulat Sharapov and Gleb Nikishin
 */
@Entity
@Getter
@Setter
@Table(name = "Photo")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uniqIdentifier_photo")
    private String uniqIdentifier;

    @ManyToOne()
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "date_photo")
    private Timestamp datePhoto;

    @Column(name = "url")
    private String url;

    @ManyToMany()
    @JoinTable(
            name = "photo_recipients",
            joinColumns = @JoinColumn(name = "photo_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id")
    )
    private List<User> recipients;
}
