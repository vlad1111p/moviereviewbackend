package demo.demo.amazonAws.controllers;

import demo.demo.amazonAws.config.form.*;
import demo.demo.amazonAws.config.jwt.JwtProvider;
import demo.demo.amazonAws.model.Role;
import demo.demo.amazonAws.model.RoleName;
import demo.demo.amazonAws.model.User;
import demo.demo.amazonAws.registration.RegistrationService;
import demo.demo.amazonAws.repository.RoleRepository;
import demo.demo.amazonAws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RegistrationService registrationService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getFirstname(), signUpRequest.getLastname(),
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        user.setUserProfileImageLink("");
        Set<Role> roles = new HashSet<>();
        if(signUpRequest.getRole() != null) {
            Set<String> strRoles = signUpRequest.getRole();

            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(adminRole);

                        break;
                    case "pm":
                        Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(pmRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(userRole);
                }
            });
        } else {
            // default mode : User register
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            roles.add(userRole);
        }

        user.setRoles(roles);
        userRepository.save(user);
        String token= registrationService.register(user);
        return new ResponseEntity<>(
                new ResponseMessage("User "+ signUpRequest.getFirstname() +
                        " is registered successfully!"), HttpStatus.OK);
    }


    @PostMapping("/currentuser")
    public ResponseEntity<UserDTO> getCurrentUser(@Valid @RequestBody JwtRequest jwtResponse) {

        //TODO Authenticate access token



        return ResponseEntity.ok().body(new UserDTO(userRepository.findByEmail(jwtResponse.getUsername()).get()));
    }
}