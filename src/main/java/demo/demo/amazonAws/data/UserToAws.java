package demo.demo.amazonAws.data;

import demo.demo.amazonAws.config.form.UserDTO;
import demo.demo.amazonAws.model.User;
import demo.demo.amazonAws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserToAws {


    private static final List<UserDTO> USER_PROFILES = new ArrayList<>();

    static {


    }

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getUserProfiles() {
        setUserProfiles();
        return USER_PROFILES;
    }

    public void setUserProfiles() {
        List<User> userRepositoryAll = userRepository.findAll();
        for (User user : userRepositoryAll) {
            UserDTO userDTO = new UserDTO(user);
            if(!USER_PROFILES.contains(userDTO))
            USER_PROFILES.add(userDTO);
        }

    }
}
