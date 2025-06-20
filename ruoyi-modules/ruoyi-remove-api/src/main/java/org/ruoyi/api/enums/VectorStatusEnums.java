package org.ruoyi.api.enums;

/**
 * @author Peng WenDeng
 * @email pengwendeng@huice.com
 * @time 2025-06-20 15-42
 * @description
 */
public enum VectorStatusEnums {
    NOT_STARTED(10, "未开始"),
    IN_PROGRESS(20, "进行中"),
    COMPLETED(30, "已完成"),
    ERROR(40, "出现异常");

    private final int code;
    private final String description;

    VectorStatusEnums(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static VectorStatusEnums fromCode(int code) {
        for (VectorStatusEnums status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的 VectorStatusEnums code: " + code);
    }
}
