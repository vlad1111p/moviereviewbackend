package demo.demo.amazonAws.service;

import demo.demo.amazonAws.model.Role;
import demo.demo.amazonAws.model.RoleName;
import demo.demo.amazonAws.model.User;
import demo.demo.amazonAws.registration.Token;
import demo.demo.amazonAws.registration.TokenService;
import demo.demo.amazonAws.repository.RoleRepository;
import demo.demo.amazonAws.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "USER WAS NOT FOUND";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;


    public User saveUser(User user) {
        log.info("save new user to the database");
        return userRepository.save(user);
    }

    public User getUser(String user) {
        log.info("returning user{}", user);
        return userRepository.findByEmail(user).get();
    }

    public List<User> getAllUsers() {
        log.info("returning all users");
        return userRepository.findAll();
    }

    public void addRoleToUser(String username, String roleName) {
        log.info("adding role {} to user {}", roleName, username);
        User user = userRepository.findByEmail(username).get();
        Role role = roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase())).get();
        user.getRoles().add(role);
    }

    public Role saveRole(Role role) {

        return roleRepository.save(role);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        log.info("user found in the database: {}", email);
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        user.getRole().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        });
        return user;

    }

    public String singUpUser(User user) {

        String tokenUuid = UUID.randomUUID().toString();
        Token token = new Token(tokenUuid,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        tokenService.saveConfirmationToken(token);

        return tokenUuid;
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userRepository.findByEmail(name).get();
    }

}
