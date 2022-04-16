package demo.demo.amazonAws.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {

    @Bean
    public AmazonS3 s3(){
        AWSCredentials credentials = new BasicAWSCredentials(
                "","");
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(String.valueOf(Region.EU_Frankfurt))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
