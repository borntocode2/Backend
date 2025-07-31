package goodspace.backend.global.swagger;

import goodspace.backend.qna.dto.QuestionRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(name = "CreateQuestionSwaggerSchema", description = "질문 생성 요청 (Swagger용)")
public class CreateQuestionSwaggerSchema {
    @Schema(description = "{\n" +
            "  \"title\": \"질문 제목입니다.\",\n" +
            "  \"content\": \"이것은 질문 내용입니다.\",\n" +
            "  \"type\": \"DELIVERY, ORDER, ITEM\"\n" +
            "}", type = "string", format = "binary")
    public QuestionRequestDto questionRequestDto;

    @Schema(description = "첨부파일 목록", type = "array", format = "binary")
    public List<MultipartFile> files;
}
