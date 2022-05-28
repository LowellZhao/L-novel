package com.lowellzhao.lnovel.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * StringUtil
 *
 * @author lowellzhao
 * @since 2022-05-28
 */
public class StringUtil {

    public static void main(String[] args) {
        System.out.println(toNumber("四亿七百六十万五千四百零九"));
        System.out.println(toNumber("一百二十"));
    }

    private static HashMap<Character, Integer> number = new HashMap<Character, Integer>() {{
        put('一', 1);
        put('二', 2);
        put('三', 3);
        put('四', 4);
        put('五', 5);
        put('六', 6);
        put('七', 7);
        put('八', 8);
        put('九', 9);
    }};

    private static HashMap<Character, Integer> digit = new HashMap<Character, Integer>() {{
        put('十', 10);
        put('百', 100);
        put('千', 1000);
        put('万', 10000);
        put('亿', 100000000);
    }};

    public static int toNumber(String str) {
        List<Integer> temp = new ArrayList<>();
        boolean ok = process(str, temp);
        int res = 0;
        if (ok) {
            for (int i : temp) {
                res += i;
            }
        }
        return res;
    }

    private static boolean process(String input, List<Integer> temp) {
        if (input.equals("")) {
            return true;
        } else if (digit.containsKey(input.charAt(0))) {
            if (temp.size() == 0 || temp.get(temp.size() - 1) >= digit.get(input.charAt(0))) {
                return false;
            }
            int cur = 0;
            while (temp.size() >= 1 && temp.get(temp.size() - 1) < digit.get(input.charAt(0))) {
                cur += temp.get(temp.size() - 1);
                temp.remove(temp.size() - 1);
            }
            temp.add(cur * digit.get(input.charAt(0)));
            return process(input.substring(1), temp);
        } else if (number.containsKey(input.charAt(0))) {
            temp.add(number.get(input.charAt(0)));
            return process(input.substring(1), temp);
        } else if (input.charAt(0) == '零') {
            return process(input.substring(1), temp);
        } else {
            return false;
        }
    }

}
