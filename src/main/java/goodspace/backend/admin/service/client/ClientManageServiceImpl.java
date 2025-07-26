package goodspace.backend.admin.service.client;

import goodspace.backend.admin.dto.client.ClientInfoResponseDto;
import goodspace.backend.admin.dto.client.ClientRegisterRequestDto;
import goodspace.backend.admin.dto.client.ClientUpdateRequestDto;
import goodspace.backend.admin.image.ImageManager;
import goodspace.backend.client.domain.Client;
import goodspace.backend.client.domain.RegisterStatus;
import goodspace.backend.global.domain.Item;
import goodspace.backend.client.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ClientManageServiceImpl implements ClientManageService {
    private static final Supplier<EntityNotFoundException> CLIENT_NOT_FOUND = () -> new EntityNotFoundException("클라이언트를 찾지 못했습니다.");
    private static final String PROFILE = "profile";
    private static final String BACKGROUND = "bg";

    private final ClientRepository clientRepository;

    private final ImageManager imageManager;

    @Override
    @Transactional(readOnly = true)
    public List<ClientInfoResponseDto> findAll() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                .map(ClientInfoResponseDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientInfoResponseDto findById(long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(CLIENT_NOT_FOUND);

        return ClientInfoResponseDto.from(client);
    }

    @Override
    @Transactional
    public ClientInfoResponseDto register(ClientRegisterRequestDto clientDto) {
        Client client = clientRepository.save(Client.builder()
                .name(clientDto.name())
                .introduction(clientDto.introduction())
                .clientType(clientDto.clientType())
                .status(RegisterStatus.PRIVATE)
                .build());

        client.setProfileImageUrl(imageManager.createImageUrl(client.getId().toString(), PROFILE, clientDto.encodedProfileImage()));
        client.setBackgroundImageUrl(imageManager.createImageUrl(client.getId().toString(), BACKGROUND, clientDto.encodedBackgroundImage()));

        return ClientInfoResponseDto.from(client);
    }

    @Override
    @Transactional
    public ClientInfoResponseDto update(ClientUpdateRequestDto requestDto) {
        Client client = clientRepository.findById(requestDto.id())
                .orElseThrow(CLIENT_NOT_FOUND);

        if (requestDto.profileUpdated()) {
            imageManager.updateImage(requestDto.encodedProfileImage(), client.getProfileImageUrl());
        }
        if (requestDto.backgroundUpdated()) {
            imageManager.updateImage(requestDto.encodedBackgroundImage(), client.getBackgroundImageUrl());
        }

        client.update(
                requestDto.name(),
                requestDto.introduction(),
                requestDto.clientType(),
                requestDto.status()
        );

        return ClientInfoResponseDto.from(client);
    }

    @Override
    @Transactional
    public void delete(long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(CLIENT_NOT_FOUND);

        removeEveryImage(client);

        clientRepository.delete(client);
    }

    private void removeEveryImage(Client client) {
        imageManager.deleteImage(client.getProfileImageUrl());
        imageManager.deleteImage(client.getBackgroundImageUrl());

        for (Item item : client.getItems()) {
            for (String imageUrl : item.getImageUrls()) {
                imageManager.deleteImage(imageUrl);
            }
        }
    }
}
