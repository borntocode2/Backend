package goodspace.backend.fixture;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

public enum ImageFixture {
    KOTLIN("kotlin.png"),
    CHOLOG("cholog.png"),
    GDG("gdg.png"),
    GOOD_SPACE("good_space.png"),
    JAVA("java.png");

    private static String DEFAULT_PARAMETER_NAME = "TEST_IMAGE_FILE";
    private static String IMAGE_CONTENT_TYPE = "image/png";
    private static String DEFAULT_BASE_PATH = "images/";

    private final String fileName;

    ImageFixture(String fileName) {
        this.fileName = fileName;
    }

    public MultipartFile getImage() {
        try {
            ClassPathResource resource = new ClassPathResource(DEFAULT_BASE_PATH + fileName);
            return new MockMultipartFile(
                    DEFAULT_PARAMETER_NAME,
                    fileName,
                    IMAGE_CONTENT_TYPE,
                    resource.getInputStream()
            );
        } catch (IOException e) {
            throw new IllegalStateException("테스트 이미지 로딩 실패: " + fileName, e);
        }
    }
}
