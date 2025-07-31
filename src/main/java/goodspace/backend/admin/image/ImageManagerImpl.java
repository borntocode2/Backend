package goodspace.backend.admin.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Base64;

@Component
public class ImageManagerImpl implements ImageManager {
    private final String baseUrl;

    public ImageManagerImpl(
            @Value("${image.base.url:images}") String baseUrl
    ) {
        this.baseUrl = trimSlash(baseUrl);
    }

    @Override
    public String createImageUrl(Object prefixUrl, Object fileName, MultipartFile image) {
        return createImageUrl(prefixUrl.toString(), fileName.toString(), image);
    }

    @Override
    public String createImageUrl(String prefixUrl, String fileName, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 없습니다.");
        }

        try {
            Path dir = getDirectory(prefixUrl);
            Files.createDirectories(dir);

            // 원본 확장자 추출
            String originalFileName = image.getOriginalFilename();
            String ext = "png";
            if (originalFileName != null && originalFileName.contains(".")) {
                ext = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
            }

            String finalFileName = fileName + "." + ext;
            Path filePath = dir.resolve(finalFileName);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return buildUrl(prefixUrl, finalFileName);
        } catch (IOException ex) {
            String debugInfo = buildDebugInfo(prefixUrl, fileName, image);

            throw new RuntimeException(debugInfo, ex);
        }
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
    public void updateImage(MultipartFile multipartFile, String imageUrl) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("업데이트할 파일이 없습니다.");
        }

        ParsedUrl parsedUrl = parseUrl(imageUrl);
        deleteImage(imageUrl);
        createImageUrl(parsedUrl.prefixUrl(), parsedUrl.fileName(), multipartFile);
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

    private String trimSlash(String s) {
        String t = s;
        if (t.startsWith("/")) t = t.substring(1);
        if (t.endsWith("/")) t = t.substring(0, t.length() - 1);
        return t;
    }

    private String buildDebugInfo(String prefixUrl, String fileName, MultipartFile image) {
        StringBuilder debugInfo = new StringBuilder();
        debugInfo.append("이미지 저장에 실패했습니다.\n")
                .append("prefixUrl: ").append(prefixUrl).append("\n")
                .append("fileName: ").append(fileName).append("\n");

        try {
            String originalFileName = image.getOriginalFilename();
            String contentType = image.getContentType();
            long size = image.getSize();

            String ext = "png";
            if (originalFileName != null && originalFileName.contains(".")) {
                ext = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
            }

            String finalFileName = fileName + "." + ext;
            Path filePath = getDirectory(prefixUrl).resolve(finalFileName);

            debugInfo.append("원본 파일명: ").append(originalFileName).append("\n")
                    .append("확장자: ").append(ext).append("\n")
                    .append("Content-Type: ").append(contentType).append("\n")
                    .append("파일 크기: ").append(size).append(" bytes\n")
                    .append("저장 경로: ").append(filePath).append("\n");
        } catch (Exception innerEx) {
            debugInfo.append("디버그 정보 생성 중 오류 발생: ").append(innerEx.getMessage());
        }

        return debugInfo.toString();
    }

    private record ParsedUrl(
            String prefixUrl,
            String fileName
    ) {

    }
}
