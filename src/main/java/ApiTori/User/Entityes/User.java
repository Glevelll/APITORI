package ApiTori.User.Entityes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Bulat Sharapov
 */
@Table(name = "tori_user", uniqueConstraints = @UniqueConstraint(columnNames = "login"))
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    @Schema(example = "0")
    private Integer id;

    @Column(name = "login", unique = true)
    @Schema(example = "john_doe")
    private String login;

    @Column(name = "password")
    @Schema(example = "secretpassword")
    private String password;

    @Column(name = "email", unique = true)
    @Schema(example = "john@example.com")
    private String email;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "friends_requests",
            joinColumns = {@JoinColumn(name = "id_user_sender")},
            inverseJoinColumns = {@JoinColumn(name = "id_user")}
    )
    private Set<User> myRequests;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "friends_requests",
            joinColumns = {@JoinColumn(name = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "id_user_sender")}
    )
    private Set<User> friendsRequests;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "friends",
            joinColumns = {@JoinColumn(name = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "id_user_friend")}
    )
    private Set<User> friends;

    /**
     * @return должен возвращать набор привелегий юзера
     * но в нашем сучае пока все что он возращает, это что
     * все пользователи имеют роль юзера.
     * все другие методы возвращают тру потому что
     * пока нет блокировки и остальных методов
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


