package demo.demo.amazonAws.profileaws;


import demo.demo.amazonAws.buckets.BucketName;
import demo.demo.amazonAws.config.form.UserDTO;
import demo.demo.amazonAws.filestore.FileStore;
import demo.demo.amazonAws.model.User;
import demo.demo.amazonAws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserProfileService {


    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService,
                              FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserDTO> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }

    void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // 1. Check if image is not empty
        isFileEmpty(file);
        // 2. If file is an image
        isImage(file);

        // 3. The user exists in our database
        UserDTO user = getUserProfileOrThrow(userProfileId);

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);

        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), "profiles",user.getUserProfileId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(filename);
            User user1 = userRepository.findByEmail(user.getEmail()).get();
            user1.setUserProfileImageLink(filename);
            userRepository.save(user1);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


    }

    byte[] downloadUserProfileImage(UUID userProfileId) {
        UserDTO user = getUserProfileOrThrow(userProfileId);

        String path = String.format("%s/%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),"profiles",
                user.getUserProfileId());
        System.out.println(path);
        return fileStore.download(path, user.getUserProfileImageLink());

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private UserDTO getUserProfileOrThrow(UUID userProfileId) {
        return userProfileDataAccessService
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }

}
