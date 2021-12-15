package cn.wjchang.common.validate.exception;

/**
 * 参数校验异常 抛出此异常 自行捕获处理
 */
public class ParamsInValidException extends RuntimeException {

    public ParamsInValidException() {}

    public ParamsInValidException(String message) {
        super(message);
    }

    public ParamsInValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsInValidException(Throwable cause) {
        super(cause);
    }

    public ParamsInValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
