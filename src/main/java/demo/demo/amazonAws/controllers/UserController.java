package demo.demo.amazonAws.controllers;


import demo.demo.amazonAws.config.form.JwtRequest;
import demo.demo.amazonAws.config.form.SimpleJsonFile;
import demo.demo.amazonAws.config.form.UserDTO;
import demo.demo.amazonAws.model.Message;
import demo.demo.amazonAws.model.Role;
import demo.demo.amazonAws.model.User;
import demo.demo.amazonAws.repository.UserRepository;
import demo.demo.amazonAws.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        userService.getAllUsers().forEach(e-> users.add(new UserDTO(e)));
        return ResponseEntity.ok().body(users);
    }

    @PostMapping(value = "/setbio")
    public void setBio(@Valid @RequestBody SimpleJsonFile simpleJsonFile) {
        User user = userRepository.findByEmail(simpleJsonFile.getEmail()).get();
        System.out.println("im here adsdadasd");
        user.setBio(simpleJsonFile.getContent());
        userRepository.save(user);
    }

    @PostMapping(value = "/setothers")
    public void setBio(@Valid @RequestBody UserDTO simpleJsonFile) {
        User user = userRepository.findByEmail(simpleJsonFile.getEmail()).get();
        System.out.println("im here adsdadasd");
        user.setFirstName(simpleJsonFile.getFirstName());
        user.setLastName(simpleJsonFile.getLastName());
        userRepository.save(user);
    }

//    @PostMapping("/getusermessages")
//    public ResponseEntity<List<Message>> getCurrentUser(@RequestParam String email) {
//
//        //TODO Authenticate access token
//
//        System.out.println(new UserDTO(userRepository.findByEmail(email).get()));
//        return ResponseEntity.ok().body(userRepository.findByEmail(email).get().getMessages());
//    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }


    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/login")
//    public Authentication attemptAuthentication(@RequestBody User user) throws AuthenticationException {
//        String username = user.getUsername();
//        String password = user.getPassword();
//        log.info("username is: {} ", username);
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//
//        return authenticationManager.authenticate(authenticationToken);
//    }

//    @PostMapping("/refreshtoken")
//    public void refreshToken(HttpServletRequest request,
//                             HttpServletResponse response) throws IOException {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                String refresh_token = authorizationHeader.substring("Bearer ".length());
//                Algorithm algorithm = Algorithm.HMAC256("dotenvonline".getBytes());
//                JWTVerifier verifier = JWT.require(algorithm).build();
//                DecodedJWT decodedJwt = verifier.verify(refresh_token);
//                String username = decodedJwt.getSubject();
//                User user = userService.getUser(username);
//
//                String access_token = JWT.create()
//                        .withSubject(user.getUsername())
//                        .withExpiresAt(new java.sql.Date(System.currentTimeMillis() + 10 * 60 * 1000))
//                        .withIssuer(request.getRequestURL().toString())
//                        .withClaim("roles", user.getRole().stream().map(Role::getName)
//                                .collect(Collectors.toList()))
//                        .sign(algorithm);
//                Map<String, String> tokens = new HashMap<>();
//                tokens.put("access_token", access_token);
//                tokens.put("refresh_token", refresh_token);
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//
//
//            } catch (Exception e) {
//                log.error("error logging in: {}", e.getMessage());
//                response.setHeader("error", e.getMessage());
//                response.setStatus(FORBIDDEN.value());
////                    response.sendError(FORBIDDEN.VALUE());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", e.getMessage());
//
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }
//        } else {
//            throw new RuntimeException("refresh token is missing");
//        }
//    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}