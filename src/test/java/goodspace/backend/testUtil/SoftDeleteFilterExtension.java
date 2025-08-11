package goodspace.backend.testUtil;

import goodspace.backend.global.soft.delete.SoftDeleteConstant;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JPA 기반 테스트 실행 전후로 SoftDelete 필터를 켜고 끄는 클래스
 * org.junit.jupiter.api.extension.Extension 파일을 통해 JUnit의 확장 클래스로 등록함
 */
public class SoftDeleteFilterExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        ApplicationContext ac = getSpringContextOrNull(context);
        if (ac == null) return; // 스프링 테스트 아님 → skip

        EntityManager em = getEntityManagerOrNull(ac);
        if (em == null) return; // JPA 컨텍스트 없음 → skip

        Session session = em.unwrap(Session.class);
        var enabled = session.getEnabledFilter(SoftDeleteConstant.FILTER_NAME);
        if (enabled == null) {
            session.enableFilter(SoftDeleteConstant.FILTER_NAME)
                    .setParameter(SoftDeleteConstant.FILTER_PARAM, false); // deleted=false만 조회
        } else {
            enabled.setParameter(SoftDeleteConstant.FILTER_PARAM, false);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        ApplicationContext ac = getSpringContextOrNull(context);
        if (ac == null) return;

        EntityManager em = getEntityManagerOrNull(ac);
        if (em == null) return;

        Session session = em.unwrap(Session.class);
        if (session.getEnabledFilter(SoftDeleteConstant.FILTER_NAME) != null) {
            session.disableFilter(SoftDeleteConstant.FILTER_NAME);
        }
    }

    private ApplicationContext getSpringContextOrNull(ExtensionContext context) {
        try {
            return SpringExtension.getApplicationContext(context);
        } catch (IllegalStateException ex) {
            // 스프링 컨텍스트 없는 순수 JUnit 테스트
            return null;
        }
    }

    private EntityManager getEntityManagerOrNull(ApplicationContext ac) {
        ObjectProvider<EntityManager> provider = ac.getBeanProvider(EntityManager.class);
        return provider.getIfAvailable();
    }
}
