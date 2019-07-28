package com.study.demo.datastruct;

import java.util.ArrayList;
import java.util.HashSet;

public class Recursion {


    public static ArrayList<String> result = new ArrayList<String>();
    public static HashSet<String> set = new HashSet<String>();

    /**
     * 求数字二进制表示形式中 1 的个数
     *
     * @param i
     * @return
     */
    public static int getCnt1(int i) {
        if (i < 0) {
            return 32 - getCnt1(-i);
        }
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 1;
        }
        if (i % 2 == 0) {
            return getCnt1(i / 2);
        } else {
            return getCnt1(i / 2) + 1;
        }
    }

    /**
     * 字符串全排列
     * @param str
     */
    public static void permute(String str) {
        permute(str.toCharArray(), 0, str.length() - 1);
    }

    public static void permute(char[] array, int start, int end) {
        String r = null;
        if (start == end) {
            r = String.valueOf(array);
            set.add(r);
        } else {
            for (int i = start; i <= end; i++) {
                /**
                 * 交换 array[start]与array[i]的位置
                 */
                char tmp = array[start];
                array[start] = array[i];
                array[i] = tmp;

                permute(array, start + 1, array.length - 1);

                /**
                 * 还原array[start]与array[i]的位置
                 */
                tmp = array[start];
                array[start] = array[i];
                array[i] = tmp;
            }
        }

    }

    public static void main(String[] args) {
        permute("abcde");
        System.out.println();
    }

}
