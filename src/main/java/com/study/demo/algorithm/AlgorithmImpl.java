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

    public static List<Integer> findReapete(int[] arr) {
        List<Integer> rs = new LinkedList<>();
        int[] arr2 = new int[Short.MAX_VALUE];
        for (int i = 0; i < arr.length; i++) {
            arr2[arr[i]]++;
            if (arr2[arr[i]] > 1) {
                rs.add(arr[i]);
            }
        }
        return rs;
    }


    /**
     * 字符串全排列
     *
     * @param str
     */
    public static Set<String> permutation(String str) {
        Set<String> s = new HashSet<>();
        permutation(str.toCharArray(), 0, str.length() - 1, s);
        return s;
    }

    /**
     * 字符串全排列
     *
     * @param chars 字符数组
     * @param start 开始位置
     * @param end
     * @param s
     */
    private static void permutation(char[] chars, int start, int end, Set<String> s) {
        if (start == end) {
            s.add(String.valueOf(chars));
        } else {
            for (int i = start; i <= end; i++) {
                char tmp = chars[i];
                chars[i] = chars[start];
                chars[start] = tmp;

                permutation(chars, start + 1, end, s);

                tmp = chars[i];
                chars[i] = chars[start];
                chars[start] = tmp;
            }
        }
    }

    /**
     * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
     *
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s) {
        /**
         * 假设字符集为 ASCII 128
         * 常用的表如下所示：

         int [26] 用于字母 ‘a’ - ‘z’ 或 ‘A’ - ‘Z’
         int [128] 用于ASCII码
         int [256] 用于扩展ASCII码
         时间复杂度：O(n)，索引 j将会迭代 n 次。  空间复杂度（Table）：O(m)，m 是字符集的大小。
         */
        int n = s.length(), ans = 0;
        int m = 128;
        int[] index = new int[m]; // current index of character
        // try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; j++) {
            i = Math.max(index[s.charAt(j)], i);
            ans = Math.max(ans, j - i + 1);
            index[s.charAt(j)] = j + 1;
        }
        return ans;
    }


    /**
     * 找出两个有序数组的中位数
     *
     * @param A
     * @param B
     * 关键点:i+j = m+n-i-j  j=(m+n)/2-i，为了让奇数时中位数落在左边，j=(m+n+1)/2-i
     *只要保证 1。 len(left_part)=len(right_part)
     *  2 。 max(left_part})<=min(right_part)
     *
     *  在 [0，m][0，m] 中搜索并找到目标对象 ii，以使：

     * B[j−1]≤A[i] 且 A[i−1]≤B[j], 其中 j=(m+n+1)/2-i
     *
     * 时间复杂度：O(log(min(m,n))),空间复杂度为 O(1)
     * @return
     */
    public static double findMedianSortedArrays(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        if (m > n) { // to ensure m<=n
            int[] temp = A;
            A = B;
            B = temp;
            int tmp = m;
            m = n;
            n = tmp;
        }
        int iMin = 0, iMax = m, halfLen = (m + n + 1) / 2;
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = halfLen - i;
            if (i < iMax && B[j - 1] > A[i]) {
                iMin = i + 1; // i is too small
            } else if (i > iMin && A[i - 1] > B[j]) {
                iMax = i - 1; // i is too big
            } else { // i is perfect
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = B[j - 1];
                } else if (j == 0) {
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                if ((m + n) % 2 == 1) {
                    return maxLeft;
                }

                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(B[j], A[i]);
                }

                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0.0;
    }

    /**
     * 最长回文子串,中心扩散法
     * @param s
     * @return
     */
    public static String longestPalindrome(String s) {
        int len = s.length();
        if (len == 0) {
            return "";
        }
        int longestPalindrome = 1;
        String longestPalindromeStr = s.substring(0, 1);
        for (int i = 0; i < len; i++) {
            String palindromeOdd = centerSpread(s, len, i, i);//奇数情况
            String palindromeEven = centerSpread(s, len, i, i + 1);//偶数情况
            String maxLen = palindromeOdd.length() > palindromeEven.length() ? palindromeOdd : palindromeEven;
            if (maxLen.length() > longestPalindrome) {
                longestPalindrome = maxLen.length();
                longestPalindromeStr = maxLen;
            }
        }
        return longestPalindromeStr;
    }

    /**
     * 中心扩散
     * @param s
     * @param len
     * @param left
     * @param right
     * @return
     */
    private static String centerSpread(String s, int len, int left, int right) {
        int l = left;
        int r = right;
        while (l >= 0 && r < len && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        // 这里要特别小心，跳出 while 循环的时候，是第 1 个满足 s.charAt(l) != s.charAt(r) 的时候
        // 所以，不能取 l，不能取 r
        return s.substring(l + 1, r);
    }


    /**
     *
      * @param x
     * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。

    示例 1:

    输入: 123
    输出: 321
     示例 2:

    输入: -123
    输出: -321
    示例 3:

    输入: 120
    输出: 21
    注意:
    假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−231,  231 − 1]。请根据这个假设，如果反转后整数溢出那么就返回 0。

     * @return
     */
    public static int inverse(int x) {
        int rs = 0;
        while (x != 0){
            int pop = x % 10;
            x /= 10;
            /**
             * rs *10 + pop可能会溢出，条件为rs*10+pop > Integer.MAX_VALUE 或者 rs*10+pop < Integer.MIN_VALUE
             */
            if(rs > (Integer.MAX_VALUE-Math.abs(pop))/10 || rs < (Integer.MIN_VALUE+Math.abs(pop))/10 ){
                return 0;
            }
            rs = rs *10 + pop;
        }
        return rs;
    }


    /**
     * 判断数字是不是回文数字
     * @param x
     *
     * 只需要返回数字的一半长度
     *
     * @return
     */
    public static boolean isPalindrome(int x){
        //先处理特殊情况
        if(x < 0 || (x%10==0 && x!= 0)){
            return false;
        }

        int revertNum = 0;
        while (x > revertNum){
            revertNum = revertNum*10+x%10;
            x /= 10;
        }
        //数字长度可能 是奇数也可能是偶数，比如 12321  1331
        //如果是12321 则revertNum=123,x=12 如果是1331 则revertNum=12 x=12
        return x == revertNum || x == revertNum/10;
    }

    /**
     * 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。

     '.' 匹配任意单个字符
     '*' 匹配零个或多个前面的那一个元素
     所谓匹配，是要涵盖 整个 字符串 s的，而不是部分字符串。

     说明:

     s 可能为空，且只包含从 a-z 的小写字母。
     p 可能为空，且只包含从 a-z 的小写字母，以及字符 . 和 *。

     * @param s
     * @param p
     * @return
     */
    public static boolean isMatch(String s, String p) {

        return false;
    }

    public static void main(String[] args) {
 /*       List<Integer> reapete = findReapete(new int[]{2, 3, 2, 4, 5, 4, 6, 7, 8});
        System.out.println(reapete);

        System.out.println(permutation("abcd"));*/
        System.out.println(lengthOfLongestSubstring("abceeefg"));

      /*  int[] a = new int[128];
        for (int i = 0; i < 128; i++) {
            a[i] = i;
        }
        System.out.println();*/
//
//        System.out.println(findMedianSortedArrays(new int[]{12,32,45,56,78},new int[]{1,2,3,900}));
//
//        System.out.println(longestPalindrome("mcbagabf"));
//
//        System.out.println(inverse(123));

        System.out.println(Integer.parseInt("-91283472332"));

    }

}
