import java.io.File;
import java.util.Arrays;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/1 10:11 上午
 * @since V1.0.0
 */
public class A {
    public static void main(String[] args) {
//        File file = new File("/Users/lzr");
//        System.out.println(file.getName());

//        Class<?> clazz = Class.forName("my.spring.framework.beans.config.AZBeanDefinition");
//        System.out.println(clazz.getName());
//        System.out.println(clazz.getSimpleName());

        int i = kmpMatch("aabaabaaf", "aaf");
        System.out.println(i);

    }

    public static int kmpMatch(String content, String pattern) {
        int[] next = getNext(pattern);
        System.out.println(Arrays.toString(next));

        char[] contentChar = content.toCharArray();
        char[] patternChar = pattern.toCharArray();

        int j = 0;  //
        for (int i = 0; i < contentChar.length; i++) {
            if (j > 0 && contentChar[i] != patternChar[j]) {
                j = next[j - 1];
            }
            if (contentChar[i] == patternChar[j]) {
                j ++;
            }
            if (j == patternChar.length) {
                return i - j + 1;
            }
        }
        return -1;
    }

    // 获得next数组（最大相同前后缀）
    public static int[] getNext(String pattern) {
        char[] patternChar = pattern.toCharArray();
        int[] next = new int[patternChar.length];  // next数组
        next[0] = 0;
        int j = 0;
        for (int i = 1; i < patternChar.length; i++) {
            while (j > 0 && patternChar[i] != patternChar[j]) {
                j = next[j - 1];
            }
            if (patternChar[i] == patternChar[j]){
                j ++;
            }
            next[i] = j;
        }
        return next;
    }

}
