package org.strauteka.jbin.demo.algorithm;

import java.util.concurrent.ThreadLocalRandom;

import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Space;

public class Utils {

    public static long concat(int a, int b, int c, int validSide) {
        final int x_ = lower(a, b, c, 0);
        final int x__ = lower(a, b, c, 1);
        final int x___ = lower(a, b, c, 2);
        return flag(concat(x_, x__, x___), x_ <= validSide, x__ <= validSide, x___ <= validSide);
    }

    private static long flag(long value, boolean a_, boolean b_, boolean c_) {
        return value + (toValue(a_) + toValue(b_) + toValue(c_)) * -1000000000000000000L;
    }

    public static long concat(int a, int b, int c) {
        return (a * 1000000000000L) + (b * 1000000L) + c + 9000000000000000000L;
    }

    private static int lower(int a, int b, int c, int pos) {
        if (pos == 0)
            return a < b ? (Math.min(c, a)) : (Math.min(c, b));
        if (pos == 1)
            return a < b ? (c < a ? a : (Math.min(c, b))) : (c < b ? b : (Math.min(c, a)));
        return a < b ? (Math.max(c, b)) : (Math.max(c, a));
    }

    private static int toValue(boolean value) {
        return value ? 1 : 0;
    }

    public static Size maxQtyOnDimension(Space space, Dimension size) {
        return new Size(space.l() / size.l(), space.h() / size.h(), space.w() / size.w());
    }

    public static int grow(int max, int step) {
        return max / step;
    }

    private static int random(int origin, int boundInclusive) {
        return ThreadLocalRandom.current().nextInt(origin, (boundInclusive + 1));
    }

    public static int randomReduce(int input) {
        if (input > 1 && random(0, 2) == 0)
            return (input - 1);

        return input;
    }
}
