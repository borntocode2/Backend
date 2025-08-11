package goodspace.backend.admin.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageManager {
    String createImageUrl(Object prefixUrl, Object fileName, MultipartFile image);

    String createImageUrl(String prefixUrl, String fileName, MultipartFile image);

    void deleteImage(String imageUrl);

    void updateImage(MultipartFile multipartFile, String imageUrl);
}
