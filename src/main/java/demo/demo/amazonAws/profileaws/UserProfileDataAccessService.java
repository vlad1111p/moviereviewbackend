package demo.demo.amazonAws.profileaws;

import demo.demo.amazonAws.config.form.UserDTO;
import demo.demo.amazonAws.data.UserToAws;
import demo.demo.amazonAws.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileDataAccessService {

    private final UserToAws userToAwsStore;

    @Autowired
    public UserProfileDataAccessService(UserToAws userToAwsStore) {
        this.userToAwsStore = userToAwsStore;
    }

    List<UserDTO> getUserProfiles() {
        return userToAwsStore.getUserProfiles();
    }
}