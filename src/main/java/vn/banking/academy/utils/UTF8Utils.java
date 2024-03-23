package vn.banking.academy.utils;

public class UTF8Utils {
    public static String decodeVietnamese(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append((char) (c - 192));
            } else if (Character.isLowerCase(c)) {
                sb.append((char) (c - 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
