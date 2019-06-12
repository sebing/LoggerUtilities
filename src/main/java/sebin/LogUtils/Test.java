package sebin.LogUtils;

import java.util.Arrays;

public class Test {
    public static void main(String args[]){
        final String string = "ABC";
        final String[] split = string.split(",");
        System.out.println(Arrays.asList(split));
        System.out.println(split.length);
    }
}
