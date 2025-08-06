package goodspace.backend.meta.dto;

import goodspace.backend.client.domain.RegisterStatus;
import lombok.Builder;

@Builder
public record RegisterStatusDto(
        String name,
        String korean
) {
    public static RegisterStatusDto from(RegisterStatus registerStatus) {
        return RegisterStatusDto.builder()
                .name(registerStatus.name())
                .korean(registerStatus.getKorean())
                .build();
    }
}
