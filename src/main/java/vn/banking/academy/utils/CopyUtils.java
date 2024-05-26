package vn.banking.academy.utils;
import org.springframework.beans.BeanUtils;

public class CopyUtils {
    public static <T> T copy(final Object src, T target) {
        if (src == null) {
            return null;
        }
        BeanUtils.copyProperties(src, target);
        return target;
    }

    public static <T> T copy(final Object src, T target, String... ignore) {
        if (src == null) {
            return null;
        }
        BeanUtils.copyProperties(src, target, ignore);
        return target;
    }
}
