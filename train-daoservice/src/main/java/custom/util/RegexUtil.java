package custom.util;

import java.util.regex.Pattern;

public class RegexUtil {

    public static final String MOBILE_PHONE_REGEX_SIMPLE = "^1[0-9]{10}$";
    public static final String MOBILE_PHONE_REGEX_COMPLEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|17[0-9]|(18[0,5-9]))\\d{8}$";
    public static final String MOBILE_PHOE_REGEX_HAS_86 ="((\\+86)|(86))?((13[0-9])|(14[5|7])|17[0-9]|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
    public static final String EMAIL_REGEX_SIMPLE ="^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    public static final  String DATE_REGEX_SIMPLE = "[0-9]{4}-[0-1]{1}[0-9]{1}-[0-3]{1}[0-9]{1} [0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}";//yyyy-MM-dd HH:mm:ss

    public static boolean check(String regex, CharSequence input) {
        return Pattern.matches(regex, input);
    }

    public static void main(String[] args) {


        System.out.println(check(MOBILE_PHOE_REGEX_HAS_86, "+8617600203527"));
        System.out.println(check(EMAIL_REGEX_SIMPLE,"134344hdknn@kdng.co"));

    }
}
