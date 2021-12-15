package cn.wjchang.common.validate.exception;

/**
 * 参数校验异常 未知异常
 *
 */
public class ParamsCheckException extends ParamsInValidException {

    public ParamsCheckException() {
    }

    public ParamsCheckException(String message) {
        super(message);
    }

    public ParamsCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsCheckException(Throwable cause) {
        super(cause);
    }

    public ParamsCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
