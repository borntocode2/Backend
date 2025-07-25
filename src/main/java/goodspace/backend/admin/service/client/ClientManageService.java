package goodspace.backend.admin.service.client;

import goodspace.backend.admin.dto.client.ClientInfoResponseDto;
import goodspace.backend.admin.dto.client.ClientRegisterRequestDto;
import goodspace.backend.admin.dto.client.ClientUpdateRequestDto;

import java.util.List;

public interface ClientManageService {
    List<ClientInfoResponseDto> findAll();

    ClientInfoResponseDto findById(long clientId);

    ClientInfoResponseDto register(ClientRegisterRequestDto requestDto);

    ClientInfoResponseDto update(ClientUpdateRequestDto requestDto);

    void delete(long clientId);
}
