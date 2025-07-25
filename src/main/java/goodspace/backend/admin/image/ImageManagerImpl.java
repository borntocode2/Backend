package goodspace.backend.admin.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

@Component
public class ImageManagerImpl implements ImageManager {
    private final String baseUrl;

    public ImageManagerImpl(
            @Value("${image.base.url}") String baseUrl
    ) {
        this.baseUrl = trimSlash(baseUrl);
    }

    @Override
    public String createImageUrl(String prefixUrl, String fileName, String encodedImage) {
        try {
            Path dir = getDirectory(prefixUrl);
            Files.createDirectories(dir);

            String[] parts = splitEncoded(encodedImage);
            String meta = parts[0];
            String data = parts[1];

            String ext = extractExtension(meta);
            String finalFileName = fileName + "." + ext;
            Path filePath = dir.resolve(finalFileName);

            byte[] bytes = Base64.getDecoder().decode(data);
            Files.write(filePath, bytes, StandardOpenOption.CREATE_NEW);

            return buildUrl(prefixUrl, finalFileName);
        } catch (IOException ex) {
            throw new RuntimeException("이미지 저장에 실패했습니다.", ex);
        }
    }

    @Override
    public String createImageUrl(String prefixUrl, Object fileName, String encodedImage) {
        return createImageUrl(prefixUrl, fileName.toString(), encodedImage);
    }

    @Override
    public String createImageUrl(Object prefixUrl, Object fileName, String encodedImage) {
        return createImageUrl(prefixUrl.toString(), fileName.toString(), encodedImage);
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            Path file = resolvePath(imageUrl);
            Files.deleteIfExists(file);
        } catch (IOException ex) {
            throw new RuntimeException("이미지 삭제에 실패했습니다.", ex);
        }
    }

    @Override
    public void updateImage(String encodedImage, String imageUrl) {
        ParsedUrl p = parseUrl(imageUrl);
        deleteImage(imageUrl);
        createImageUrl(p.prefixUrl(), p.fileName(), encodedImage);
    }

    private Path getDirectory(String prefixUrl) {
        String cleaned = trimSlash(prefixUrl);
        return cleaned.isEmpty()
                ? Paths.get(baseUrl)
                : Paths.get(baseUrl, cleaned.split("/"));
    }

    private String buildUrl(String prefixUrl, String fileNameExt) {
        String cleanedPrefixUrl = trimSlash(prefixUrl);
        
        return "/" + baseUrl
                + (cleanedPrefixUrl.isEmpty() ? "" : "/" + cleanedPrefixUrl)
                + "/" + fileNameExt;
    }

    private String[] splitEncoded(String encoded) {
        String[] parts = encoded.split(",", 2);
        if (parts.length < 2) {
            return new String[]{"", parts[0]};
        }
        
        return parts;
    }

    private ParsedUrl parseUrl(String imageUrl) {
        try {
            String relativeUrl = convertToRelativeUrl(imageUrl);
            Path fullPath = Paths.get(baseUrl, relativeUrl.split("/"));
            Path basePath = Paths.get(baseUrl);
            Path relativePath = basePath.relativize(fullPath);

            int segmentCount = relativePath.getNameCount();
            String fileNameWithExtension = extractFileNameWithExtension(relativePath, segmentCount);
            String fileName = removeExtension(fileNameWithExtension);

            String prefixUrl = "";
            if (segmentCount > 1) {
                prefixUrl = "/" + relativePath.subpath(0, segmentCount - 1).toString().replace("\\", "/");
            }

            return new ParsedUrl(prefixUrl, fileName);
        } catch (URISyntaxException ex) {
            throw new RuntimeException("이미지 URL 파싱에 실패했습니다.", ex);
        }
    }

    private String convertToRelativeUrl(String imageUrl) throws URISyntaxException {
        String path = imageUrl;
        if (path.startsWith("http://") || path.startsWith("https://")) {
            path = new URI(path).getPath();
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        String prefix = baseUrl + "/";
        if (!path.startsWith(prefix)) {
            throw new IllegalArgumentException("잘못된 이미지 URL입니다: " + imageUrl);
        }

        return path.substring(prefix.length());
    }

    private String extractFileNameWithExtension(Path relativePath, int segmentCount) {
        return relativePath.getName(segmentCount - 1).toString();
    }

    private String removeExtension(String fileNameWithExtension) {
        return fileNameWithExtension.replaceFirst("\\.[^.]+$", "");
    }

    private Path resolvePath(String imageUrl) {
        try {
            String path = imageUrl;
            if (path.startsWith("http://") || path.startsWith("https://")) {
                URI uri = new URI(path);
                path = uri.getPath();
            }
            if (path.startsWith("/")) path = path.substring(1);

            String prefix = baseUrl + "/";
            if (!path.startsWith(prefix)) {
                throw new IllegalArgumentException("잘못된 이미지 URL입니다: " + imageUrl);
            }

            String relative = path.substring(prefix.length());
            String[] parts = relative.split("/");
            return Paths.get(baseUrl, parts);
        } catch (URISyntaxException ex) {
            throw new RuntimeException("이미지 URL 파싱에 실패했습니다.", ex);
        }
    }

    private String extractExtension(String meta) {
        if (meta.startsWith("data:") && meta.contains(";base64")) {
            String mime = meta.substring(5, meta.indexOf(';'));
            String ext = mime.substring(mime.indexOf('/') + 1);
            int plus = ext.indexOf('+');
            if (plus > 0) ext = ext.substring(0, plus);
            return ext;
        }
        return "png";
    }

    private String trimSlash(String s) {
        String t = s;
        if (t.startsWith("/")) t = t.substring(1);
        if (t.endsWith("/")) t = t.substring(0, t.length() - 1);
        return t;
    }

    private record ParsedUrl(
            String prefixUrl,
            String fileName
    ) {
    }
}
