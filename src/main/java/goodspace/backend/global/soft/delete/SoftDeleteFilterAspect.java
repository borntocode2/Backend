package goodspace.backend.global.soft.delete;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class SoftDeleteFilterAspect {

    private final EntityManager em;
    private final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);

    @Around(
            "@within(org.springframework.transaction.annotation.Transactional) || " +
                    "@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object aroundTransactional(ProceedingJoinPoint pjp) throws Throwable {
        var method = ((MethodSignature) pjp.getSignature()).getMethod();
        var declaringClass = method.getDeclaringClass();

        boolean disable = method.isAnnotationPresent(DisableSoftDeleteFilter.class)
                || declaringClass.isAnnotationPresent(DisableSoftDeleteFilter.class);
        boolean onlyDeleted = method.isAnnotationPresent(ViewOnlyDeleted.class)
                || declaringClass.isAnnotationPresent(ViewOnlyDeleted.class);

        if (disable) {
            return pjp.proceed();
        }

        var session = em.unwrap(org.hibernate.Session.class);
        boolean outermost = depth.get() == 0;

        if (outermost) {
            session.enableFilter(SoftDeleteConstant.FILTER_NAME)
                    .setParameter(SoftDeleteConstant.FILTER_PARAM, onlyDeleted); // deleted = true/false
        } else {
            var f = session.getEnabledFilter(SoftDeleteConstant.FILTER_NAME);
            if (f != null) f.setParameter(SoftDeleteConstant.FILTER_PARAM, onlyDeleted);
        }

        depth.set(depth.get() + 1);
        try {
            return pjp.proceed();
        } finally {
            depth.set(depth.get() - 1);
            if (outermost) {
                session.disableFilter(SoftDeleteConstant.FILTER_NAME);
            }
            if (depth.get() == 0) depth.remove();
        }
    }
}

