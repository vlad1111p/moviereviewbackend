package demo.demo.amazonAws.config.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleJsonFile {
    @JsonProperty("content")
    private String content;
    @JsonProperty("email")
    private String email;

    public SimpleJsonFile(String content, String email) {
        this();
        this.content = content;
        this.email = email;
    }

    public SimpleJsonFile() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
