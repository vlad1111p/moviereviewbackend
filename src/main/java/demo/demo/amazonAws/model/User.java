package demo.demo.amazonAws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity(name = "User")
@AllArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private UUID userProfileId;
    @Column(columnDefinition = "varchar(255)")
    private String bio;
    @JsonIgnore
    @ManyToMany( cascade = {CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH},
            fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Message> messages;
    private String password;
    private String userProfileImageLink;
    private Boolean locked = false;
    private Boolean enabled = false;

    public User(String firstName, String lastName, String email, UUID userProfileId, String bio, Role roles,
                String password, String userProfileImageLink) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userProfileId = userProfileId;
        this.bio = bio;

        this.password = password;
        this.userProfileImageLink = userProfileImageLink;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && firstName.equals(user.firstName) && lastName.equals(user.lastName) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email);
    }

    public User(String firstName, String lastName, String email, String password, Role roles, UUID userProfileId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles.add(roles);
        this.userProfileId = userProfileId;
    }

    public User(String email, String bio, String password) {
        this.email = email;
        this.userProfileId = UUID.randomUUID();
        this.bio = bio;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, UUID userProfileId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userProfileId = userProfileId;
    }

    public User(String firstName, String lastName, String username, String encode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = username;
        this.password = encode;
        this.userProfileId=UUID.randomUUID();
    }

    public Optional<String> getUserProfileImageLink() {
        return Optional.ofNullable(userProfileImageLink);
    }

    public void setUserProfileImageLink(String userProfileImageLink) {
        this.userProfileImageLink = userProfileImageLink;
    }

    @Override
    public Collection getAuthorities() {
        return getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
