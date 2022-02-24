package demo.demo.amazonAws.config.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("accessToken")
    private String accessToken;


}