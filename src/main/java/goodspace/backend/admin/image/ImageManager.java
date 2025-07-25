package goodspace.backend.admin.image;

public interface ImageManager {
    String createImageUrl(String prefixUrl, String fileName, String encodedImage);

    String createImageUrl(String prefixUrl, Object fileName, String encodedImage);

    String createImageUrl(Object prefixUrl, Object fileName, String encodedImage);

    void deleteImage(String imageUrl);

    void updateImage(String encodedImage, String imageUrl);
}
