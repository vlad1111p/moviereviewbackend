package demo.demo.amazonAws.buckets;

public enum BucketName {
    PROFILE_IMAGE("moviereviews-123");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
