package goodspace.backend.global.soft.delete;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SoftDeleteConstant {
    public static final String FILTER_NAME = "softDelete";
    public static final String FILTER_PARAM = "isDeleted";
    public static final String DELETED_COLUMN = "deleted";
    public static final String CONDITION = DELETED_COLUMN + " = :" + FILTER_PARAM;
}
