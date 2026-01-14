package com.greenbox.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.ArrayList;
import java.util.List;
public class StringUtils {
    public static String upperFirstLetter(String str) {
        if(org.apache.commons.lang3.StringUtils.isEmpty(str)){
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String lowerFirstLetter(String str) {
        if(org.apache.commons.lang3.StringUtils.isEmpty(str)){
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 判定空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str) || "null".equals(str) || "\u0000".equals(str)) {
            return true;
        } else if (" ".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static Boolean isNumber(String str){
        String regex = "^[0-9]+$";
        if(isEmpty(str))
            return false;
        return str.matches(regex);
    }

    public static final String getRandomNumber(Integer length) {
        return RandomStringUtils.random(length,false,true);
    }

    public static final String getRandomString(Integer length) {
        return RandomStringUtils.random(length,true  ,true);
    }

    public static String encodeMd5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String getFileSuffix(String fileName) {
            if (isEmpty(fileName)) {
                return null;
            }
            return fileName.substring(fileName.lastIndexOf("."));
        }

    public static Integer[] convertStringToIntArray(String input) {
        if (isEmpty(input)) {
            return null;
        }

        String[] parts = input.split(";");
        List<Integer> list = new ArrayList<>();

        for (String part : parts) {
            String trimmed = part.trim();
            if (!isEmpty(trimmed)) {
                list.add(Integer.parseInt(trimmed));
            }
        }

        Integer[] result = new Integer[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    public static String cleanHtmlTag(String content) {
        if (isEmpty(content)) {
            return content;
        }
        //注意先替换尖括号！！！
        //防止信息script注入
        content = content.replace("<","&lt");
        //换行转换
        content = content.replace("\r\n","<br>" );
        content = content.replace("\n","<br>" );
        return content;
    }
}
