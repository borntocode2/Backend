package goodspace.backend.fixture;

import goodspace.backend.client.domain.Client;
import goodspace.backend.client.domain.ClientType;

public enum ClientFixture {
    INFLUENCER(
            "INFLUENCER KIM",
            "http://influencer-kim-profile",
            "http://influencer-kim-bg",
            "Hello, I'm Influencer Kim!",
            ClientType.INFLUENCER
    ),
    CREATOR(
            "CREATOR PARK",
            "http://creator-park-profile",
            "http://creator-park-bg",
            "Hello, I'm Creator Park!",
            ClientType.CREATOR
    ),
    YOUTUBER(
            "YOUTUBER LEE",
            "http://youtuber-lee-profile",
            "http://youtuber-lee-bg",
            "Hello, I'm Youtuber Lee!",
            ClientType.CREATOR
    );

    private final String name;
    private final String profileImageUrl;
    private final String backgroundImageUrl;
    private final String introduction;
    private final ClientType clientType;

    ClientFixture(
            String name,
            String profileImageUrl,
            String backgroundImageUrl,
            String introduction,
            ClientType clientType
    ) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
        this.introduction = introduction;
        this.clientType = clientType;
    }

    public Client getInstance() {
        return Client.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .backgroundImageUrl(backgroundImageUrl)
                .introduction(introduction)
                .clientType(clientType)
                .build();
    }
}
