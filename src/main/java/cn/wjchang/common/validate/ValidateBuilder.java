package cn.wjchang.common.validate;

import cn.wjchang.common.validate.check.Check;
import cn.wjchang.common.validate.exception.ParamsCheckException;
import cn.wjchang.common.validate.exception.ParamsInValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ValidateBuilder {

    /**
     * 请使用build方法构造
     */
    private ValidateBuilder() {
    }

    /**
     * 验证相关信息的容器保存类
     */
    private final List<ValidateChain> chains = new ArrayList<ValidateChain>();

    /**
     * 实体类
     */
    public static class ValidateChain {

        private Check check;
        private Object value;
        private String express;
        private Boolean result;
        private String msg;

        public Check getCheck() {
            return check;
        }

        public void setCheck(Check check) {
            this.check = check;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getExpress() {
            return express;
        }

        public void setExpress(String express) {
            this.express = express;
        }

        public Boolean getResult() {
            return result;
        }

        public void setResult(Boolean result) {
            this.result = result;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    /**
     * @param check   验证的枚举方法 自定义请使用Custom枚举
     * @param value   验证的值
     * @param express 表达式 非必填 和枚举方法相关
     * @param msg     提示信息 没有默认取枚举方法的提示信息
     * @return
     */
    public ValidateBuilder vali(Check check, Object value, String express, String msg) {
        ValidateChain validateChain = new ValidateChain();
        validateChain.setCheck(check);
        validateChain.setValue(value);
        validateChain.setExpress(express);
        validateChain.setMsg(msg == null || Objects.equals(msg, "") ? check.msg : msg);
        chains.add(validateChain);
        return this;
    }

    public ValidateBuilder vali(Check check, Object value, String msg) {
        return vali(check, value, null, msg);
    }

    public ValidateBuilder vali(Check check, Object value) {
        return vali(check, value, null, null);
    }

    public ValidateBuilder wvali(Check check, Object value, String express) {
        return vali(check, value, express, null);
    }

    /**
     * 真正检查的方法
     *
     * @return ValidateBuilder
     */
    public ValidateBuilder doCheck() {
        for (ValidateChain conn : chains) {
            Boolean result = conn.getCheck().check(conn.value, conn.express);
            conn.setResult(result);
        }
        return this;
    }

    /**
     * 此次验证是否通过
     *
     * @return Boolean
     */
    public Boolean isPassed() {
        if (chains == null || chains.isEmpty()) {
            throw new ParamsCheckException("chains can not be empty, please call methods  'vali' and 'doCheck' first");
        }
        List<ValidateChain> fields = chains.stream().filter(e -> Boolean.FALSE.equals(e.getResult())).collect(Collectors.toList());
        return fields.size() <= 0;
    }

    /**
     * 不通过就抛出异常
     * ParamsInValidException
     *
     * @return ValidateBuilder
     */
    public ValidateBuilder ifNotPassedThrowException() {
        return ifNotPassedThrowException(null);
    }

    /**
     * 不通过就抛出异常
     *
     * @return ValidateBuilder
     */
    public ValidateBuilder ifNotPassedThrowException(RuntimeException e) {
        Boolean passed = isPassed();
        if (e == null) {
            e = new ParamsInValidException(getFailedMsgs());
        }
        if (!passed) {
            throw e;
        }
        return this;
    }

    /**
     * 获取失败的消息
     * 没有失败消息返回null
     *
     * @return String
     */
    public String getFailedMsgs() {
        if (chains == null || chains.isEmpty()) {
            return null;
        }
        String failedMsgs = chains.stream().filter(e -> Boolean.FALSE.equals(e.getResult()))
                .map(e ->  e.getMsg() + ":" + e.getValue() + " " + (e.getExpress() == null ? "" : e.getExpress()))
                .collect(Collectors.joining(","));

        return "".equals(failedMsgs) ? null : failedMsgs;
    }

    /**
     * 获取失败的条数
     */
    public int getFailedCounts() {
        if (chains == null || chains.isEmpty()) {
            return 0;
        }
        return chains.stream()
                .filter(e -> Boolean.FALSE.equals(e.getResult()))
                .collect(Collectors.toList())
                .size();
    }

    /**
     * 获取成功的条数
     */
    public int getSuccessCounts() {
        return chains.size() - getFailedCounts();
    }

    public List<ValidateChain> getChains() {
        return chains;
    }

    public static ValidateBuilder build() {
        return new ValidateBuilder();
    }

    public ValidateBuilder clear() {
        chains.clear();
        return this;
    }
}
