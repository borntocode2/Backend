package goodspace.backend.qna.dto;

import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionFileDto {
    @Lob
    private byte[] data;

    private String extension;
    private String mimeType;
    private String name;
}
