package goodspace.backend.testUtil;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.function.Supplier;

@Component
public class ImageUtil {
    private static final Supplier<FileNotFoundException> FILE_NOT_FOUND = () -> new FileNotFoundException("해당 경로에 파일이 존재하지 않습니다.");

    public boolean isSameImage(String imageUrl, String encodedImage) throws IOException {
        byte[] decodedImage = getDecodedImage(encodedImage);

        return isSameImage(imageUrl, decodedImage);
    }

    public boolean isSameImage(String imageUrl, byte[] decodedImage) throws IOException {
        Path path = getPathFromUrl(imageUrl);
        if (!Files.exists(path)) {
            throw FILE_NOT_FOUND.get();
        }

        return Arrays.equals(Files.readAllBytes(path), decodedImage);
    }

    public byte[] getImageFromUrl(String url) throws IOException {
        Path path = getPathFromUrl(url);

        return Files.readAllBytes(path);
    }

    private Path getPathFromUrl(String url) {
        return Path.of(url.replaceFirst("^/", ""));
    }

    private byte[] getDecodedImage(String encodedImage) {
        return Base64
                .getDecoder()
                .decode(encodedImage);
    }
}
