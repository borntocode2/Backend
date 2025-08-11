package goodspace.backend.global.soft.delete;

import java.lang.annotation.*;

/**
 * deleted = true로 설정된 엔티티만 조회하도록 하는 어노태이션
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewOnlyDeleted {}
