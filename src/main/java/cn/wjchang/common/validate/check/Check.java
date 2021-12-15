package cn.wjchang.common.validate.check;


import java.util.function.BiFunction;

/**
 * 验证方法 枚举
 */
public enum Check {

    Null("参数必须为空", CheckUtil::isNull),
    NotNull("参数必须不为空", CheckUtil::isNotNull),
    Empty("参数的必须为空", CheckUtil::isEmpty),
    NotEmpty("参数必须非空", CheckUtil::isNotEmpty),

    True("参数必须为 true", CheckUtil::isTrue),
    False("参数必须为 false", CheckUtil::isFalse),

    Date("参数必须是一个日期 yyyy-MM-dd", CheckUtil::isDate),
    DateTime("参数必须是一个日期时间 yyyy-MM-dd HH:mm:ss", CheckUtil::isDateTime),
    TimeMillSeconds("参数必须是一个时间毫秒值", CheckUtil::isTimeMillSeconds),
    Past("参数必须是一个过去的日期", CheckUtil::isPast),
    Future("参数必须是一个将来的日期", CheckUtil::isFuture),
    Today("参数必须今天的日期", CheckUtil::isToday),

    Number("参数必须是数字类型", CheckUtil::isNumber),
    Chinese("参数必须是汉字", CheckUtil::isChinese),
    General("参数必须是英文字母 、数字和下划线", CheckUtil::isGeneral),
    GeneralWithChinese("参数必须是中文字、英文字母、数字和下划线", CheckUtil::isGeneralWithChinese),

    IN("参数必须在合适的范围内", CheckUtil::inRange),
    NotIn("参数必须不在指定的范围内", CheckUtil::outRange),
    Length("参数长度必须在指定范围内", CheckUtil::inLength),
    Enum("参数必须在枚举中", CheckUtil::inEnum),

    GT("参数必须大于指定值", CheckUtil::isGreaterThan),
    LT("参数必须小于指定值", CheckUtil::isLessThan),
    GTE("参数必须大于等于指定值", CheckUtil::isGreaterThanEqual),
    LTE("参数必须小于等于指定值", CheckUtil::isLessThanEqual),
    NEQ("参数必须不等于指定值", CheckUtil::isNotEqual),
    EQ("参数必须等于指定值", CheckUtil::isEqual),

    Pattern("参数必须符合指定的正则表达式", CheckUtil::isPattern),
    Currency("参数必须是货币", CheckUtil::isCurrency),
    CreditCode("参数必须是社会统一信用代码", CheckUtil::isCreditCode),
    CitizenId("参数必须是身份证号", CheckUtil::isCitizenId),
    Email("参数必须是Email地址", CheckUtil::isEmail),
    Mobile("参数必须是手机号", CheckUtil::isMobilePhone),
    Url("参数必须是一个完整的url", CheckUtil::isUrl),
    ISBN("参数必须是一个书籍ISBN编号", CheckUtil::isISBN),
    BankNumber("参数必须是一个银行卡号", CheckUtil::isBankNumber),
    ChinesePostCode("参数必须是中国邮编", CheckUtil::isChinesePostCode),
    PlateNumber("参数必须是中国车牌号", CheckUtil::isPlateNumber),
    UUID("参数必须是UUID", CheckUtil::isUUID),
    Ipv4("参数必须是ipv4", CheckUtil::isIpv4),
    Ipv6("参数必须是ipv6", CheckUtil::isIpv6),
    Mac("参数必须是mac地址", CheckUtil::isMac);


    /**
     * msg 信息
     */
    public final String msg;

    /**
     * BiFunction：接收字段值(Object)和 表达式(String)，返回是否符合规则(Boolean)
     */
    public final BiFunction<Object, String, Boolean> fun;

    Check(String msg, BiFunction<Object, String, Boolean> fun) {
        this.msg = msg;
        this.fun = fun;
    }

    public Boolean check(Object value, String express) {
        return this.fun.apply(value, express);
    }

    public Boolean check(Object value) {
        return check(value, null);
    }
}