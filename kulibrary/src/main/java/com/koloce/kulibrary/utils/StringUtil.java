package com.koloce.kulibrary.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.koloce.kulibrary.base.BaseApp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String getRealUriPath(Context context, Uri fileUrl) {
        String fileName = null;
        if (fileUrl != null) {
            if (fileUrl.getScheme().toString().compareTo("content") == 0) // content://开头的uri
            {
                Cursor cursor = context.getContentResolver().query(fileUrl, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    try {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        fileName = cursor.getString(column_index); // 取出文件路径
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } finally {
                        cursor.close();
                    }
                }
            } else if (fileUrl.getScheme().compareTo("file") == 0) // file:///开头的uri
            {
                fileName = fileUrl.getPath();
            }
        }
        return fileName;
    }

    /**
     * 把一个long类型的总毫秒数转成时长
     */
    public static String getDurationText(long mms) {
        int hours = (int) (mms / (1000 * 60 * 60));
        int minutes = (int) ((mms % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((mms % (1000 * 60)) / 1000);
        String s = "";
        if (hours > 0) {
            if (hours < 10) {
                s += "0" + hours + ":";
            } else {
                s += hours + ":";
            }
        }
        if (minutes > 0) {
            if (minutes < 10) {
                s += "0" + minutes + ":";
            } else {
                s += minutes + ":";
            }
        } else {
            s += "00" + ":";
        }
        if (seconds > 0) {
            if (seconds < 10) {
                s += "0" + seconds;
            } else {
                s += seconds;
            }
        } else {
            s += "00";
        }
        return s;
    }


    /**
     * 字符串转Base64
     *
     * @param str
     * @return
     */
    public static String encodeToString(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }

    /**
     * 字符串Base64解码
     *
     * @return
     */
    public static String decode(String str) {
        if (StringUtil.isEmpty(str)){
            return "";
        }
        try {
            byte[] decode = Base64.decode(str, Base64.DEFAULT);
            String s = new String(decode);
            if (StringUtil.isEmpty(s)){
                return str;
            }
            return s;
        } catch (Exception e){
            return str + "";
        }
    }

    @NonNull
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检查EditText值是否为空
     *
     * @param editText
     * @param notice   如果为空的提示信息
     * @return
     */
    public static boolean checkEditIsEmpty(EditText editText, String notice) {
        if (isEmpty(editText.getText().toString())) {
            if (!isEmpty(notice)) {
                Toast.makeText(BaseApp.getContext(), notice, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    /**
     * 检查EditText值是否为空（如果为空，提示信息为hint值）
     *
     * @param editText
     * @return
     */
    public static boolean checkEditIsEmpty(EditText editText) {
        return checkEditIsEmpty(editText, editText.getHint().toString());
    }

    /**
     * 字符串是否为空（包括null或者NULL都算空）
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        if (s == "")
            return true;
        if ("null".equals(s))
            return true;
        if ("NULL".equals(s))
            return true;
        return false;
    }

    /**
     * byte kb m g 转换
     *
     * @param size 单位byte
     * @return
     */
    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 校验Tag Alias 只能是数字,英文字母和中文
     *
     * @param s
     * @return
     */
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 只保留数字
     *
     * @param str
     * @return
     */
    public static String getNumber(String str) {
        if (str == null) {
            return "";
        }
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 编码
     *
     * @param string
     * @return
     */
    public static String URLEncodeUTF_8(String string) {
        return Uri.encode(string);
    }

    /**
     * 解码
     *
     * @param string
     * @return
     */
    public static String URLDecodeUTF_8(String string) {
        if (StringUtil.isEmpty(string)) return string;
        return Uri.decode(string);

    }

    /**
     * @param countent 文本内容
     * @param find     查找字符
     * @return 次数
     */
    public static int getStringCount(String countent, String find) {
        int count = 0;
        int per = -1;//用于保存前一次的索引位置
        for (int i = 0; i < countent.length(); i++) {
            if (countent.indexOf(find, i) != -1) {
                if (countent.indexOf(find, i) != per) {//如果这次的找到的索引和上次不一样,那么才算一次
                    per = countent.indexOf(find, i);//记录这次找到的索引
                    count++;//次数+1;

                }
            }
            // indexOf(String str, int fromIndex)
            // 返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始。
            // 也就是说 比如 天涯天涯 中查找  天涯
            // 第一次循环 天涯天涯 包含了天涯 出现次数1次,索引位置 0;
            // 第二次循环 涯天涯 包含了天涯 出现次数2次,索引位置2;
            // 第三次循环 天涯 包含了天涯 出现次数3次,索引位置还是2;
            // 第四次循环 涯 没有查找到天涯,索引位置-1;
            // 所以一定要判断 索引位置是否相同,相同就不要重复计算次数了
        }

        return count;
    }

    /**
     * 去除一段文本最前面 和 最后面的换行
     * 如:
     * 前：/n/n试试/n/n再试试/n/n
     * 后：试试/n/n再试试
     *
     * @param content
     * @return
     */
    public static String substringTrimN(String content) {
        String substring = content;
        int length = content.length();
        for (int i = 0; i < length; i++) {
            String item = content.substring(length - i - 1, length - i);
            if (item.equals("\n")) {
                substring = content.substring(0, length - i - 1);
            } else {
                break;
            }
        }

        for (int i = 0; i < substring.length(); i++) {
            String item2 = substring.substring(0, 1);
            if (item2.equals("\n")) {
                substring = substring.substring(1);
            } else {
                break;
            }
        }
        return substring;
    }

    /**
     * 是不是超链接
     *
     * @return
     */
    public static boolean isLink(String url) {
        String regEx = "(https?://|ftp://|file://|www)[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";// url
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    /**
     * 判断字符串，手机号码隐藏中间四位
     *
     * @param phone
     * @return
     */
    public static String getSafePhone(String phone) {
        if (isEmpty(phone)) {
            return "";
        }
        String phoneNumber = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phoneNumber;

    }

    /**
     * 判断字符串，银行卡号隐藏中间八位
     *
     * @param bankCard
     * @return
     */
    public static String getSafeCardNum(String bankCard) {
        if (isEmpty(bankCard)) return "";
        int hideLength = 8;//替换位数
        int sIndex = bankCard.length() / 2 - hideLength / 2;
        String replaceSymbol = "*";
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < bankCard.length(); i++) {
            char number = bankCard.charAt(i);
            if (i >= sIndex - 1 && i < sIndex + hideLength) {
                sBuilder.append(replaceSymbol);
            } else {
                sBuilder.append(number);
            }
        }
        return sBuilder.toString();
    }


    /**
     * 判断是否是手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobilePhone(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]|(19[0-9])))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * @param parentUrl https://m.kankanwu.com/Sciencefiction/chulingkongjian/
     * @return chulingkongjian
     */
    public static String urlConverAfter(String parentUrl) {
        String mUrl = parentUrl.substring(0, parentUrl.lastIndexOf("/"));
        return mUrl.substring(mUrl.lastIndexOf("/") + 1);
    }


    /**
     * @param parentUrl https://m.kankanwu.com/Sciencefiction/chulingkongjian/
     * @return https://m.kankanwu.com
     */
    public static String urlConverBefore(String parentUrl) {
        return parentUrl.substring(0, parentUrl.indexOf("m/") + 1);
    }


    /**
     * 判断字符串是否是数字
     *
     * @return
     */
    public static boolean isNumberic(String str) {
        if (isEmpty(str)) return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 是否是中文
     *
     * @param c
     * @return
     */

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;

    }

    /**
     * 是否是英文
     *
     * @param charaString
     * @return
     */

    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");

    }

    public static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find())
            return true;
        else
            return false;
    }


    /**
     * 18位身份证校验,粗略的校验
     *
     * @param idCard
     * @return
     * @author lyl
     */
    public static boolean isIdCardNumber(String idCard) {
        Pattern pattern1 = Pattern.compile("^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$"); //粗略的校验
        Matcher matcher = pattern1.matcher(idCard);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }


    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean isBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断字符串中是否含有表情
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i + 1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() - 1) {
                    char ls = source.charAt(i + 1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return isEmoji;
    }

    /**
     * 判断字符是否是emoji
     * @param codePoint
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {

        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuffer buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuffer();
                }
                buf.append(codePoint);
            }
        }

        if (buf == null) {
            return source;//如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }

    }
}
