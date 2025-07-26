package goodspace.backend.fixture;

import goodspace.backend.client.domain.Client;
import goodspace.backend.client.domain.ClientType;
import goodspace.backend.client.domain.RegisterStatus;

public enum ClientFixture {
    INFLUENCER(
            "INFLUENCER KIM",
            "http://influencer-kim-profile",
            "http://influencer-kim-bg",
            "Hello, I'm Influencer Kim!",
            ClientType.INFLUENCER,
            RegisterStatus.PUBLIC
    ),
    CREATOR(
            "CREATOR PARK",
            "http://creator-park-profile",
            "http://creator-park-bg",
            "Hello, I'm Creator Park!",
            ClientType.CREATOR,
            RegisterStatus.PUBLIC
    ),
    YOUTUBER(
            "YOUTUBER LEE",
            "http://youtuber-lee-profile",
            "http://youtuber-lee-bg",
            "Hello, I'm Youtuber Lee!",
            ClientType.CREATOR,
            RegisterStatus.PUBLIC
    ),
    SINGER(
            "SIGNER JSON",
            "json/profile",
            "json/bg",
            "Hello, I'm Singer Json!",
            ClientType.CREATOR,
            RegisterStatus.PUBLIC
    ),
    PRIVATE_CLIENT(
            "PRIVATE CLIENT",
            "private/profile",
            "private/bg",
            "Hello, I'm Private Client!",
            ClientType.CREATOR,
            RegisterStatus.PRIVATE
    );

    private final String name;
    private final String profileImageUrl;
    private final String backgroundImageUrl;
    private final String introduction;
    private final ClientType clientType;
    private final RegisterStatus status;

    ClientFixture(
            String name,
            String profileImageUrl,
            String backgroundImageUrl,
            String introduction,
            ClientType clientType,
            RegisterStatus status
    ) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
        this.introduction = introduction;
        this.clientType = clientType;
        this.status = status;
    }

    public Client getInstance() {
        return Client.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .backgroundImageUrl(backgroundImageUrl)
                .introduction(introduction)
                .clientType(clientType)
                .status(status)
                .build();
    }
}
