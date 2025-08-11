package goodspace.backend.testUtil;

import goodspace.backend.global.soft.delete.SoftDeleteConstant;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * BeforeEachCallback을 Spring 빈으로 등록해서 필터를 활성화할 수 있게 만드는 클래스
 */
@TestConfiguration
@RequiredArgsConstructor
public class TestSoftDeleteConfig {
    @Bean
    public static BeforeEachCallback enableSoftDeleteFilterCallback(EntityManager em) {
        return context -> em.unwrap(Session.class)
                .enableFilter(SoftDeleteConstant.FILTER_NAME)
                .setParameter(SoftDeleteConstant.FILTER_PARAM, false);
    }
}
