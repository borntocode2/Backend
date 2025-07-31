package goodspace.backend.admin.service;

import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.repository.UserRepository;
import goodspace.backend.global.security.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminInitializeService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String email;
    private final String password;

    public AdminInitializeService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.email:default@adminEmail.com}") String email,
            @Value("${admin.password:defaultAdminPassword}") String password
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.password = password;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        GoodSpaceUser user = GoodSpaceUser.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        user.addRole(Role.USER);
        user.addRole(Role.ADMIN);

        saveIfNotExist(user);
    }

    private void saveIfNotExist(GoodSpaceUser user) {
        userRepository.findGoodSpaceUserByEmail(user.getEmail())
                .orElseGet(() -> userRepository.save(user));
    }
}
