package com.chong.mcspczuul;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    private static final String REGEX = ".*foo+";
    private static final String INPUT = "fooooooooooooooooo";
    private static final String INPUT2 = "ooooofoooooooooooo";
    private static Pattern pattern;
    private static Matcher matcher;
    private static Matcher matcher2;

    public static void main(String[] args) {
        // testStudyRegex();

        String whiteApi = "/order/{orderId}";
        String requestURI = "/order/1";
        String regex = whiteApi.replaceAll("\\/\\{[a-zA-Z0-9]+\\}", "\\/[a-zA-Z0-9]+");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.matches()) {
           System.out.println("匹配成功："+requestURI);
        }
    }

    public static void testStudyRegex(){
        pattern = Pattern.compile(REGEX);
        matcher = pattern.matcher(INPUT);
        matcher2 = pattern.matcher(INPUT2);

        System.out.println("Current REGEX is: " + REGEX);
        System.out.println("Current INPUT is: " + INPUT);
        System.out.println("Current INPUT2 is: " + INPUT2);


        System.out.println("lookingAt1(): " + matcher.lookingAt());
        System.out.println("matches1(): " + matcher.matches());
        System.out.println("lookingAt2(): " + matcher2.lookingAt());

        String test1 = "order111";
        String test2 = "order111";
        boolean rlt = test2.matches(test1);


        String originStr = "/order/{orderId}/{detailId}";
        String evalueStr = "/order/1/11/1";
        String targetStr = originStr.replaceAll("\\/\\{[a-zA-Z0-9]*\\}",
                "\\/[a-zA-Z0-9]*");
        System.out.println(targetStr);
        pattern = Pattern.compile(targetStr);
        matcher = pattern.matcher(evalueStr);
        if (matcher.matches()) {
            System.out.println("匹配成功："+evalueStr);
        }else {
            System.out.println("匹配失败："+evalueStr);
        }
    }
}