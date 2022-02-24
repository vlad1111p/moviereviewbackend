package demo.demo.amazonAws.config.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import demo.demo.amazonAws.model.Message;
import demo.demo.amazonAws.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UUID userProfileId;
    private String bio;
    private List<String> messages = new ArrayList<>();
    private String userProfileImageLink;

    public UserDTO(Long id, String firstName, String lastName, String email, UUID userProfileId, String bio, List<String> messages, String userProfileImageLink) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userProfileId = userProfileId;
        this.bio = bio;
        this.messages = messages;
        this.userProfileImageLink = userProfileImageLink;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.userProfileId = user.getUserProfileId();
        this.bio = user.getBio();
        for (Message message : user.getMessages()) {
            this.messages.add(message.getContent());
        }
        this.userProfileImageLink = user.getUserProfileImageLink().get();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(email, userDTO.email) && Objects.equals(userProfileId, userDTO.userProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, userProfileId);
    }
}
