package com.study.demo.algorithm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by heyanwei-thinkpad on 2019/7/2.
 */
public class AlgorithmImpl {


    /**
     * 有一个数组[2,3,2,4,5,4,6,7,8],只有两个元素出现了两次，其它都是一次
     * 写出一个方法，找出重复的元素，要求时间复杂度O(n)空间复杂度O(1)
     */

    public static List<Integer> findReapete(int[] arr){
        List<Integer> rs = new LinkedList<>();
        int[] arr2 = new int[Short.MAX_VALUE];
        for (int i = 0; i < arr.length; i++) {
            arr2[arr[i]]++;
            if(arr2[arr[i]] > 1){
                rs.add(arr[i]);
            }
        }
        return rs;
    }


    /**
     * 字符串全排列
     * @param str
     */
    public static Set<String>  permutation(String str){
        Set<String> s = new HashSet<>();
        permutation(str.toCharArray(),0,str.length()-1,s);
        return s;
    }

    /**
     * 字符串全排列
     * @param chars 字符数组
     * @param start 开始位置
     * @param end
     * @param s
     */
    private static void permutation(char[] chars, int start, int end, Set<String> s) {
        if(start == end){
            s.add(String.valueOf(chars));
        }else{
            for (int i = start; i <= end; i++) {
                char tmp = chars[i];
                chars[i] = chars[start];
                chars[start] = tmp;

                permutation(chars,start+1,end,s);

                tmp = chars[i];
                chars[i] = chars[start];
                chars[start] = tmp;
            }
        }
    }

    public static void main(String[] args) {
        List<Integer> reapete = findReapete(new int[]{2, 3, 2, 4, 5, 4, 6, 7, 8});
        System.out.println(reapete);

        System.out.println(permutation("abcd"));
    }

}
