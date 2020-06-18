package com.wswl.entity;


import lombok.Getter;

/**
 * 公用错误码
 *
 * @author
 * @version
 */
@Getter
public enum BaseEnumError implements IEnumError {

    SUCCESS(200, "操作成功"),
    FAIL(1401, "操作失败"),
    WALLETFAIL(1401, "钱包操作失败");

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String msg;

    BaseEnumError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}