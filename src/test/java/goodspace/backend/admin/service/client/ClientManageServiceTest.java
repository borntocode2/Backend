package goodspace.backend.admin.service.client;

import goodspace.backend.admin.dto.client.ClientInfoResponseDto;
import goodspace.backend.admin.dto.client.ClientRegisterRequestDto;
import goodspace.backend.admin.dto.client.ClientUpdateRequestDto;
import goodspace.backend.admin.image.ImageManager;
import goodspace.backend.admin.image.ImageManagerImpl;
import goodspace.backend.client.domain.Client;
import goodspace.backend.client.domain.ClientType;
import goodspace.backend.fixture.ClientFixture;
import goodspace.backend.fixture.ImageFixture;
import goodspace.backend.client.repository.ClientRepository;
import goodspace.backend.testUtil.ImageUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ClientManageServiceTest {
    static final Supplier<EntityNotFoundException> CLIENT_NOT_EXIST = () -> new EntityNotFoundException("클라이언트가 존재하지 않습니다.");

    static final String DEFAULT_NAME = "defaultClient";
    static final String DEFAULT_PROFILE_IMAGE = ImageFixture.KOTLIN.encodedImage;
    static final String DEFAULT_BACKGROUND_IMAGE = ImageFixture.CHOLOG.encodedImage;
    static final String DEFAULT_INTRODUCTION = "hello";
    static final ClientType DEFAULT_CLIENT_TYPE = ClientType.CREATOR;

    static final String NEW_NAME = "newClientName";
    static final String NEW_PROFILE_IMAGE = ImageFixture.GDG.encodedImage;
    static final String NEW_BACKGROUND_IMAGE = ImageFixture.GOOD_SPACE.encodedImage;
    static final String NEW_INTRODUCTION = "newIntroduction";
    static final ClientType NEW_CLIENT_TYPE = ClientType.INFLUENCER;

    ClientManageService clientManageService;
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ImageUtil imageUtil;

    Client clientA;
    Client clientB;
    List<Client> existClients;

    @TempDir
    Path basePath;
    ImageManager imageManager;

    @BeforeEach
    void resetEntities() {
        imageManager = new ImageManagerImpl(basePath.toString());
        clientManageService = new ClientManageServiceImpl(clientRepository, imageManager);

        clientA = clientRepository.save(ClientFixture.CREATOR.getInstance());
        clientA.setProfileImageUrl(imageManager.createImageUrl("clientA", "profile", DEFAULT_PROFILE_IMAGE));
        clientA.setBackgroundImageUrl(imageManager.createImageUrl("clientA", "bg", DEFAULT_BACKGROUND_IMAGE));

        clientB = clientRepository.save(ClientFixture.INFLUENCER.getInstance());
        clientB.setProfileImageUrl(imageManager.createImageUrl("clientB", "profile", DEFAULT_PROFILE_IMAGE));
        clientB.setBackgroundImageUrl(imageManager.createImageUrl("clientB", "bg", DEFAULT_BACKGROUND_IMAGE));

        existClients = List.of(clientA, clientB);
    }

    @Nested
    class findAll {
        @Test
        @DisplayName("모든 클라이언트의 정보를 반환한다")
        void returnInfoOfEveryClient() {
            List<ClientInfoResponseDto> clients = clientManageService.findAll();

            assertThat(clients.size())
                    .isEqualTo(existClients.size());

            for (Client existClient : existClients) {
                ClientInfoResponseDto clientDto = findClientDtoById(clients, existClient.getId());
                assertThat(isEqual(existClient, clientDto)).isTrue();
            }
        }
    }

    @Nested
    class findById {
        @Test
        @DisplayName("ID와 일치하는 클라이언트의 정보를 반환한다")
        void returnClientInfoFindById() {
            ClientInfoResponseDto clientDto = clientManageService.findById(clientA.getId());

            assertThat(isEqual(clientA, clientDto)).isTrue();
        }
    }

    @Nested
    class register {
        @Test
        @DisplayName("새로운 클라이언트를 저장한다")
        void saveNewClient() throws IOException {
            // given
            ClientRegisterRequestDto requestDto = ClientRegisterRequestDto.builder()
                    .name(DEFAULT_NAME)
                    .encodedProfileImage(DEFAULT_PROFILE_IMAGE)
                    .encodedBackgroundImage(DEFAULT_BACKGROUND_IMAGE)
                    .introduction(DEFAULT_INTRODUCTION)
                    .clientType(DEFAULT_CLIENT_TYPE)
                    .build();

            // when
            ClientInfoResponseDto registeredClientDto = clientManageService.register(requestDto);

            // then
            assertThat(isEqualWithoutImage(registeredClientDto, requestDto)).isTrue();

            boolean isSameProfileImage = imageUtil.isSameImage(registeredClientDto.profileImageUrl(), DEFAULT_PROFILE_IMAGE);
            assertThat(isSameProfileImage).isTrue();
            boolean isSameBackgroundImage = imageUtil.isSameImage(registeredClientDto.backgroundImageUrl(), DEFAULT_BACKGROUND_IMAGE);
            assertThat(isSameBackgroundImage).isTrue();
        }

        @Test
        @DisplayName("인코딩된 이미지를 통해 이미지 URL을 생성해 저장한다")
        void saveImageUrlByEncodedImage() throws IOException {
            // given
            ClientRegisterRequestDto requestDto = ClientRegisterRequestDto.builder()
                    .name(DEFAULT_NAME)
                    .encodedProfileImage(DEFAULT_PROFILE_IMAGE)
                    .encodedBackgroundImage(DEFAULT_BACKGROUND_IMAGE)
                    .introduction(DEFAULT_INTRODUCTION)
                    .clientType(DEFAULT_CLIENT_TYPE)
                    .build();

            // when
            ClientInfoResponseDto registeredClientDto = clientManageService.register(requestDto);

            // then
            boolean profileImageNotChanged = imageUtil.isSameImage(registeredClientDto.profileImageUrl(), DEFAULT_PROFILE_IMAGE);
            assertThat(profileImageNotChanged).isTrue();

            boolean backgroundImageNotChanged = imageUtil.isSameImage(registeredClientDto.backgroundImageUrl(), DEFAULT_BACKGROUND_IMAGE);
            assertThat(backgroundImageNotChanged).isTrue();
        }
    }

    @Nested
    class update {
        @Test
        @DisplayName("클라이언트의 정보를 업데이트한다")
        void updateClient() throws IOException {
            // given
            ClientUpdateRequestDto requestDto = ClientUpdateRequestDto.builder()
                    .id(clientA.getId())
                    .name(NEW_NAME)
                    .encodedProfileImage(NEW_PROFILE_IMAGE)
                    .profileUpdated(true)
                    .encodedBackgroundImage(NEW_BACKGROUND_IMAGE)
                    .backgroundUpdated(true)
                    .introduction(NEW_INTRODUCTION)
                    .clientType(NEW_CLIENT_TYPE)
                    .build();

            // when
            clientManageService.update(requestDto);

            // then
            assertThat(isEqualWithoutImage(clientA, requestDto)).isTrue();

            assertThat(imageUtil.isSameImage(clientA.getProfileImageUrl(), NEW_PROFILE_IMAGE)).isTrue();
            assertThat(imageUtil.isSameImage(clientA.getBackgroundImageUrl(), NEW_BACKGROUND_IMAGE)).isTrue();
        }

        @Test
        @DisplayName("이미지는 새로 변경하지 않았다면 업데이트하지 않는다")
        void updateImageOnlyChanged() throws IOException {
            // given
            byte[] existProfileImage = imageUtil.getImageFromUrl(clientA.getProfileImageUrl());
            byte[] existBackgroundImage = imageUtil.getImageFromUrl(clientA.getBackgroundImageUrl());

            ClientUpdateRequestDto requestDto = ClientUpdateRequestDto.builder()
                    .id(clientA.getId())
                    .name(NEW_NAME)
                    .profileUpdated(false)
                    .backgroundUpdated(false)
                    .introduction(NEW_INTRODUCTION)
                    .clientType(NEW_CLIENT_TYPE)
                    .build();

            // when
            clientManageService.update(requestDto);

            // then
            assertThat(isEqualWithoutImage(clientA, requestDto)).isTrue();

            assertThat(imageUtil.isSameImage(clientA.getProfileImageUrl(), existProfileImage)).isTrue();
            assertThat(imageUtil.isSameImage(clientA.getBackgroundImageUrl(), existBackgroundImage)).isTrue();
        }
    }

    @Nested
    class delete {
        @Test
        @DisplayName("ID와 일치하는 클라이언트를 삭제한다")
        void deleteClientById() {
            clientManageService.delete(clientA.getId());

            boolean isDeleted = clientRepository.findById(clientA.getId())
                    .isEmpty();

            assertThat(isDeleted).isTrue();
        }
    }

    private ClientInfoResponseDto findClientDtoById(List<ClientInfoResponseDto> dtos, long clientId) {
        return dtos.stream()
                .filter(client -> client.id().equals(clientId))
                .findAny()
                .orElseThrow(CLIENT_NOT_EXIST);
    }

    private boolean isEqual(Client client, ClientInfoResponseDto dto) {
        return client.getId().equals(dto.id()) &&
                client.getName().equals(dto.name()) &&
                client.getProfileImageUrl().equals(dto.profileImageUrl()) &&
                client.getBackgroundImageUrl().equals(dto.backgroundImageUrl()) &&
                client.getIntroduction().equals(dto.introduction()) &&
                client.getClientType() == dto.clientType() &&
                client.getStatus() == dto.status();
    }

    private boolean isEqualWithoutImage(ClientInfoResponseDto clientDto, ClientRegisterRequestDto requestDto) {
        return clientDto.name().equals(requestDto.name()) &&
                clientDto.introduction().equals(requestDto.introduction()) &&
                clientDto.clientType() == requestDto.clientType();
    }

    private boolean isEqualWithoutImage(Client client, ClientUpdateRequestDto dto) {
        return client.getId().equals(dto.id()) &&
                client.getName().equals(dto.name()) &&
                client.getIntroduction().equals(dto.introduction()) &&
                client.getClientType() == dto.clientType() &&
                client.getStatus() == dto.status();
    }
}
