package com.dnz.local.buxs.utils;

public class Random {
    private static String chars = "qwertyuiopasdfghjklmnbvcxz1234567890QWERTYUIOPLKJHGFDSAZXCVBNM";

    @org.jetbrains.annotations.NotNull
    public static String getRandomString(int size) {
        StringBuilder stringBuilder = new StringBuilder();
        int stringSize = chars.length() - 1;
        int random;
        while (size > 0) {
            random = (int) (Math.random() * stringSize);
            stringBuilder.append(chars.charAt(random));
            size -= 1;
        }

        return stringBuilder.toString();
    }
}
