package goodspace.backend.global.soft.delete;

import java.lang.annotation.*;

/**
 * Soft delete 필터를 꺼서 모든 엔티티를 조회하도록 하는 어노태이션
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableSoftDeleteFilter {}
