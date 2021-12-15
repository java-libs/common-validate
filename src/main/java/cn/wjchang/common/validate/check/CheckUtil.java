package cn.wjchang.common.validate.check;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.wjchang.common.validate.check.RegexPattern.*;


/**
 * 验证方法
 */
public class CheckUtil {

    /**
     * 判断value == null
     *
     * @param value   字段值
     * @param express 这里不需要，只是为了参数统一
     * @return true or false
     */
    public static Boolean isNull(Object value, String express) {
        if (null != value) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 判断value != null
     *
     * @param value   字段值
     * @param express 这里不需要，只是为了参数统一
     * @return true or false
     */
    public static Boolean isNotNull(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 判断value ==null || length size <= 0
     * 支持字符串判断
     * 支持集合判断
     *
     */
    public static Boolean isEmpty(Object value, String express) {
        return !isNotEmpty(value, express);
    }


    /**
     * 判断value !=null && length、size > 0
     * 支持字符串判断
     * 支持集合判断
     *
     */
    public static Boolean isNotEmpty(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String && "".equals(((String) value).trim())) {
            return Boolean.FALSE;
        }
        if (value instanceof Collection && ((Collection<?>) value).size() == 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 判断参数是否是 true
     * 支持Boolean类型
     * 支持String类型
     *
     */
    public static Boolean isTrue(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            try {
                return Boolean.parseBoolean((String) value);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 判断参数是否是 false
     * 支持Boolean类型
     * 支持String类型
     *
     */
    public static Boolean isFalse(Object value, String express) {
        return !isTrue(value, express);
    }


    /**
     * 判断参数是否是一个日期
     * 支持Date类型
     * 支持LocalDate类型
     * 支持String类型，yyyy-MM-dd、yyyyMMdd、yyyy/MM/dd格式； 默认仅支持yyyy-MM-dd
     *
     */
    public static Boolean isDate(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd";
        }
        if (value instanceof String) {
            String v = ((String) value);
            try {
                LocalDate.parse(v, DateTimeFormatter.ofPattern(express));
                return Boolean.TRUE;
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return Boolean.TRUE;
        }
        if (value instanceof LocalDate) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    /**
     * 判断参数是否是一个日期
     * 支持Date类型
     * 支持LocalDateTime类型
     * 支持String类型，yyyy-MM-dd HH:mm:ss、yyyyMMddHHmmss、yyyy/MM/dd HH:mm:ss格式； 默认仅支持yyyy-MM-dd HH:mm:ss
     *
     */
    public static Boolean isDateTime(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd HH:mm:ss";
        }
        // 通常json格式参数，都是以字符串类型传递，优先判断
        if (value instanceof String) {
            //.replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            String v = ((String) value);
            try {
                LocalDateTime.parse(v, DateTimeFormatter.ofPattern(express));
                return Boolean.TRUE;
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return Boolean.TRUE;
        }
        if (value instanceof LocalDateTime) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 判断参数是否是一个过去时间
     * 支持Date类型
     * 支持LocalDate类型
     * 支持LocalDateTime类型
     * 支持String类型，yyyy-MM-dd、yyyyMMdd、yyyy/MM/dd格式； 默认仅支持yyyy-MM-dd
     * 支持String类型，yyyy-MM-dd HH:mm:ss、yyyyMMddHHmmss、yyyy/MM/dd HH:mm:ss格式； 默认仅支持yyyy-MM-dd HH:mm:ss
     *
     */
    public static Boolean isPast(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd HH:mm:ss";
        }
        if (value instanceof String) {   // 通常json格式参数，都是以字符串类型传递，优先判断
            String v = ((String) value); //.replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            try {
                LocalDateTime ldt = LocalDateTime.parse(v, DateTimeFormatter.ofPattern(express));
                return LocalDateTime.now().isAfter(ldt);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return new Date().after((Date) value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().isAfter((LocalDate) value);
        }
        if (value instanceof LocalDateTime) {
            return LocalDateTime.now().isAfter((LocalDateTime) value);
        }
        return Boolean.FALSE;
    }

    /**
     * 判断参数是否是一个将来时间
     * 支持Date类型
     * 支持LocalDate类型
     * 支持LocalDateTime类型
     * 支持String类型，yyyy-MM-dd、yyyyMMdd、yyyy/MM/dd格式; 默认仅支持yyyy-MM-dd
     * 支持String类型，yyyy-MM-dd HH:mm:ss、yyyyMMddHHmmss、yyyy/MM/dd HH:mm:ss格式； 默认仅支持yyyy-MM-dd HH:mm:ss
     *
     */
    public static Boolean isFuture(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd HH:mm:ss";
        }
        // 通常json格式参数，都是以字符串类型传递，优先判断
        if (value instanceof String) {
            // .replaceAll("[-/]", "");   验证参数，不能处理掉所有异常的符号
            String v = ((String) value);
            try {
                LocalDateTime ldt = LocalDateTime.parse(v, DateTimeFormatter.ofPattern(express));
                return LocalDateTime.now().isBefore(ldt);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return new Date().before((Date) value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().isBefore((LocalDate) value);
        }
        if (value instanceof LocalDateTime) {
            return LocalDateTime.now().isBefore((LocalDateTime) value);
        }
        return Boolean.FALSE;
    }


    /**
     * 判断是否是今天的日期
     * 支持Date类型
     * 支持LocalDate类型
     * 支持String类型，默认仅支持yyyy-MM-dd
     *
     */
    public static Boolean isToday(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (express == null || "".equals(express)) {
            express = "yyyy-MM-dd HH:mm:ss";
        }
        // 通常json格式参数，都是以字符串类型传递，优先判断
        if (value instanceof String) {
            // .replaceAll("[-/]", "");  // 验证参数，不能处理掉所有异常的符号
            String v = ((String) value);
            try {
                LocalDate ld = LocalDate.parse(v, DateTimeFormatter.ofPattern(express));
                return LocalDate.now().equals(ld);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        if (value instanceof Date) {
            return new Date().equals(value);
        }
        if (value instanceof LocalDate) {
            return LocalDate.now().equals(value);
        }
        return Boolean.FALSE;
    }

    /**
     * 是否数字类型 包括小数
     *
     */
    public static Boolean isNumber(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        if (value instanceof Number) {
            return Boolean.TRUE;
        }
        return NUMBER_CODE.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为英文字母 、数字和下划线
     *
     */
    public static Boolean isGeneral(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return GENERAL.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为中文字、英文字母、数字和下划线
     *
     */
    public static Boolean isGeneralWithChinese(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return GENERAL_WITH_CHINESE.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否是汉字
     *
     */
    public static Boolean isChinese(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return CHINESE.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否手机号
     *
     * @param value   参数值
     * @param express 空
     */
    public static Boolean isMobilePhone(Object value, String express) {
        if (null == value) {
            return Boolean.FALSE;
        }
        return MOBILE.matcher(String.valueOf(value)).matches();
    }


    /**
     * 判断是否是邮箱
     * 使用正则表达式判断
     *
     */
    public static Boolean isEmail(Object value, String express) {
        if (isNull(value, express)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            return EMAIL.matcher(String.valueOf(value)).matches();
        }
        return Boolean.FALSE;
    }

    /**
     * 判断参数的取值范围，逗号隔开，无空格；闭区间
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     *
     */
    public static Boolean inRange(Object value, String rangeStr) {
        if (isNull(value, rangeStr)) {
            return Boolean.FALSE;
        }
        if (null == rangeStr || "".equals(rangeStr)) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            int begin = Integer.parseInt(rangeStr.split(",")[0]);
            int end = Integer.parseInt(rangeStr.split(",")[1]);
            int v = ((Integer) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Long) {
            long begin = Long.parseLong(rangeStr.split(",")[0]);
            long end = Long.parseLong(rangeStr.split(",")[1]);
            long v = ((Long) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Short) {
            short begin = Short.parseShort(rangeStr.split(",")[0]);
            short end = Short.parseShort(rangeStr.split(",")[1]);
            short v = ((Short) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Float) {
            float begin = Float.parseFloat(rangeStr.split(",")[0]);
            float end = Float.parseFloat(rangeStr.split(",")[1]);
            float v = ((Float) value);
            return begin <= v && v <= end;
        }
        if (value instanceof Double) {
            double begin = Double.parseDouble(rangeStr.split(",")[0]);
            double end = Double.parseDouble(rangeStr.split(",")[1]);
            double v = ((Double) value);
            return begin <= v && v <= end;
        }
        if (value instanceof BigDecimal) {
            BigDecimal begin = new BigDecimal(rangeStr.split(",")[0]);
            BigDecimal end = new BigDecimal(rangeStr.split(",")[1]);
            BigDecimal v = ((BigDecimal) value);
            return begin.compareTo(v) <= 0 && v.compareTo(end) <= 0;
        }

        return Boolean.FALSE;
    }


    /**
     * 等价于  !Range
     *
     */
    public static Boolean outRange(Object value, String rangeStr) {
        return !inRange(value, rangeStr);
    }


    /**
     * 判断参数的取值范围，逗号隔开，无空格；闭区间
     * 判断String的length范围, rangeStr取值举例："6,18"
     *
     */
    public static Boolean inLength(Object value, String rangeStr) {
        if (isNull(value, rangeStr)) {
            return Boolean.FALSE;
        }
        if (null == rangeStr || "".equals(rangeStr)) {
            return Boolean.FALSE;
        }
        String splitter = ",";
        if (value instanceof String) {
            Integer begin = null;
            Integer end = null;
            if (!rangeStr.contains(splitter)) {
                begin = 0;
            } else {
                begin = Integer.valueOf(rangeStr.split(splitter)[0]);
            }
            if (begin == 0) {
                end = Integer.valueOf(rangeStr);
            } else {
                end = Integer.valueOf(rangeStr.split(splitter)[1]);
            }
            int v = ((String) value).length();
            return begin <= v && v <= end;
        }
        return Boolean.FALSE;
    }


    /**
     * 判断参数是否在枚举的数据中, 枚举的表达式用 英文逗号隔开，无空格，如： "男,女,太监"
     * 校验过程，不在对表达式进行校验，所以请确保表达式的格式正确
     * 支持String
     * 支持Integer Short Long
     *
     */
    public static Boolean inEnum(Object value, String enumStr) {
        if (isNull(value, null)) {
            return Boolean.FALSE;
        }
        if (null == enumStr || "".equals(enumStr)) {
            return Boolean.FALSE;
        }
        String[] array = enumStr.split(",");
        Set<String> set = new HashSet<>(Arrays.asList(array));
        return set.contains(value.toString());
    }


    /**
     * 是否大于指定值
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     * 支持String，判断length值
     * 支持Collection，判断size的值
     *
     */
    public static Boolean isGreaterThan(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) > Integer.parseInt(express);
        }
        if (value instanceof Long) {
            return ((Long) value) > Long.parseLong(express);
        }
        if (value instanceof Short) {
            return ((Short) value) > Short.parseShort(express);
        }
        if (value instanceof Float) {
            return ((Float) value) > Float.parseFloat(express);
        }
        if (value instanceof Double) {
            return ((Double) value) > Double.parseDouble(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) > 0;
        }
        if (value instanceof String) {
            return ((String) value).length() > Integer.parseInt(express);
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).size() > Integer.parseInt(express);
        }
        return Boolean.FALSE;
    }


    /**
     * 是否大于等于
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     * 支持String，判断length值
     * 支持Collection，判断size的值
     */
    public static Boolean isGreaterThanEqual(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) >= Integer.parseInt(express);
        }
        if (value instanceof Long) {
            return ((Long) value) >= Long.parseLong(express);
        }
        if (value instanceof Short) {
            return ((Short) value) >= Short.parseShort(express);
        }
        if (value instanceof Float) {
            return ((Float) value) >= Float.parseFloat(express);
        }
        if (value instanceof Double) {
            return ((Double) value) >= Double.parseDouble(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) >= 0;
        }
        if (value instanceof String) {
            return ((String) value).length() >= Integer.parseInt(express);
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).size() >= Integer.parseInt(express);
        }
        return Boolean.FALSE;

    }

    /**
     * 是否少于
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     * 支持String，判断length值
     * 支持Collection，判断size的值
     */
    public static Boolean isLessThan(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) < Integer.parseInt(express);
        }
        if (value instanceof Long) {
            return ((Long) value) < Long.parseLong(express);
        }
        if (value instanceof Short) {
            return ((Short) value) < Short.parseShort(express);
        }
        if (value instanceof Float) {
            return ((Float) value) < Float.parseFloat(express);
        }
        if (value instanceof Double) {
            return ((Double) value) < Double.parseDouble(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) < 0;
        }
        if (value instanceof String) {
            return ((String) value).length() < Integer.parseInt(express);
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).size() < Integer.parseInt(express);
        }
        return Boolean.FALSE;
    }

    /**
     * 是否少于等于
     * 支持Integer、Long、Short、Float、Double、BigDecimal
     * 支持String，判断length值
     * 支持Collection，判断size的值
     *
     */
    public static Boolean isLessThanEqual(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof Integer) {
            return ((Integer) value) <= Integer.parseInt(express);
        }
        if (value instanceof Long) {
            return ((Long) value) <= Long.parseLong(express);
        }
        if (value instanceof Short) {
            return ((Short) value) <= Short.parseShort(express);
        }
        if (value instanceof Float) {
            return ((Float) value) <= Float.parseFloat(express);
        }
        if (value instanceof Double) {
            return ((Double) value) <= Double.parseDouble(express);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) <= 0;
        }
        if (value instanceof String) {
            return ((String) value).length() <= Integer.parseInt(express);
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).size() <= Integer.parseInt(express);
        }
        return Boolean.FALSE;
    }

    /**
     * 判断是否 notEqual指定的值
     * 支持String、Integer、Long、Short、Float、Double、BigDecimal
     * 支持Collection，判断size的值
     *
     */
    public static Boolean isNotEqual(Object value, String express) {
        return !isEqual(value, express);
    }

    /**
     * 判断是否Equal指定的值
     * 支持String、Integer、Long、Short、Float、Double、BigDecimal
     * 支持Collection，判断size的值
     */
    public static Boolean isEqual(Object value, String express) {
        if (value == null) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            return value.equals(express);
        }
        if (value instanceof Integer) {
            return value.equals(Integer.valueOf(express));
        }
        if (value instanceof Long) {
            return value.equals(Long.valueOf(express));
        }
        if (value instanceof Short) {
            return value.equals(Short.valueOf(express));
        }
        if (value instanceof Float) {
            return value.equals(Float.valueOf(express));
        }
        if (value instanceof Double) {
            return value.equals(Double.valueOf(express));
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo(new BigDecimal(express)) == 0;
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).size() == Integer.parseInt(express);
        }
        return Boolean.FALSE;
    }


    /**
     * 判断String是否满足正则表达式
     */
    public static Boolean isPattern(Object value, String regEx) {
        if (isNull(value, null)) {
            return Boolean.FALSE;
        }
        if (value instanceof String) {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher((String) value);
            if (m.matches()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    /**
     * 时间毫秒值
     *
     */
    public static Boolean isTimeMillSeconds(Object value, String regEx) {
        Boolean number = isNumber(value, regEx);
        if (number) {
            return String.valueOf(value).length() == 13;
        }
        return false;
    }

    /**
     * 是否是身份证号
     *
     */
    public static Boolean isCitizenId(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return CITIZEN_ID.matcher(String.valueOf(value)).matches();
    }


    /**
     * 是否货币
     */
    public static Boolean isCurrency(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return CURRENCY.matcher(String.valueOf(value)).matches();
    }

    /**
     * 社会统一信用代码
     */
    public static Boolean isCreditCode(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return CREDIT_CODE.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否是中国邮编
     *
     */
    public static Boolean isChinesePostCode(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return ZIP_CODE.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否是Ipv4
     *
     */
    public static Boolean isIpv4(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return IPV4.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否是Ipv6
     */
    public static Boolean isIpv6(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return IPV6.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为MAC地址
     *
     */
    public static Boolean isMac(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return MAC_ADDRESS.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为中国车牌号
     *
     */
    public static Boolean isPlateNumber(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return PLATE_NUMBER.matcher(String.valueOf(value)).matches();
    }

    /**
     * 验证是否为URL
     *
     */
    public static Boolean isUrl(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        try {
            new java.net.URL(String.valueOf(value));
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    /**
     * 验证是否是图书的合法的ISBN号码 包括10或者13位的ISBN
     */
    @SuppressWarnings("warn")
    public static Boolean isISBN(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return RegexPattern.ISBN_REGEX.matcher(String.valueOf(value)).matches();
    }

    /**
     * 是否是银行卡号
     * Luhn算法来验证:
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     *
     */
    public static Boolean isBankNumber(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        String number = String.valueOf(value);
        if (number.length() != 16 && number.length() != 19) {
            return false;
        }
        if (!number.matches("\\d+")) {
            return false;
        }

        char[] digits = number.toCharArray();
        int len = number.length();
        int numSum = 0;
        for (int i = len - 1, j = 1; i >= 0; i--, j++) {
            int value0 = digits[i] - '0';
            if (j % 2 == 0) {
                value0 *= 2;
                if (value0 > 9) {
                    value0 -= 9;
                }
            }
            numSum += value0;
        }
        return numSum % 10 == 0;
    }

    /**
     * 验证是否为UUID
     * 包括带横线标准格式和不带横线的简单模式
     *
     */
    @SuppressWarnings("warn")
    public static Boolean isUUID(Object value, String regEx) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return RegexPattern.UUID.matcher(String.valueOf(value)).matches() || UUID_SIMPLE.matcher(String.valueOf(value)).matches();
    }
}
