package org.noear.solon.extend.validation;

public class StringUtils {
    /**
     * 是否全是数字
     */
    public static boolean isDigits(String str) {
        if (str != null && str.length() != 0) {
            int l = str.length();

            for (int i = 0; i < l; ++i) {
                if (!Character.isDigit(str.codePointAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isInteger(String str){
        return isNumberDo(str, false);
    }

    public static boolean isNumber(String str) {
        return isNumberDo(str, true);
    }

    /**
     * 是否为数值（可以是整数 或 小数 或 负数）
     */
    private static boolean isNumberDo(String str, boolean incDot) {
        if (str != null && str.length() != 0) {
            char[] chars = str.toCharArray();
            int l = chars.length;

            int start = chars[0] != '-' && chars[0] != '+' ? 0 : 1;
            boolean hasDot = false;

            for (int i = start; i < l; ++i) {
                int ch = chars[i];

                if(incDot) {
                    if (ch == 46) {
                        if (hasDot) {
                            return false;
                        } else {
                            hasDot = true;
                            continue;
                        }
                    }
                }

                if (!Character.isDigit(ch)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
