package org.strauteka.jbin.core;

import java.util.stream.Stream;

import org.strauteka.jbin.core.Utils.Rotation;

public class Size implements Dimension {
    private final int l;
    private final int h;
    private final int w;

    public Size(int l, int h, int w) {
        this.l = l;
        this.h = h;
        this.w = w;
    }

    public Size(Dimension dimensions) {
        this.l = dimensions.l();
        this.h = dimensions.h();
        this.w = dimensions.w();
    }

    public int l() {
        return l;
    }

    public int h() {
        return h;
    }

    public int w() {
        return w;
    }

    @Override
    public Size rotate(Rotation rotation) {
        switch (rotation) {
        case lhw:
            return new Size(l(), h(), w());
        case whl:
            return new Size(w(), h(), l());
        case wlh:
            return new Size(w(), l(), h());
        case hlw:
            return new Size(h(), l(), w());
        case hwl:
            return new Size(h(), w(), l());
        default:
            // lwh
            return new Size(l(), w(), h());
        }
    }

    public Size subtract(Dimension dimension) {
        return new Size(l() - dimension.l(), h() - dimension.h(), w() - dimension.w());
    }

    public boolean fitAny(Dimension dimension) {
        return Stream.of(Rotation.values()).map(e -> dimension.rotate(e)).filter(e -> fit(e)).findAny().isPresent();
    }

    public boolean fit(Dimension dimension) {
        final Dimension s = subtract(dimension);
        return s.l() >= 0 && s.h() >= 0 && s.w() >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Size)) {
            return false;
        } else {
            return l == ((Size) o).l() && h == ((Size) o).h() && w == ((Size) o).w();
        }
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(l).hashCode() * Integer.valueOf(h).hashCode() * Integer.valueOf(w).hashCode();
    }

    @Override
    public String toString() {
        return String.format("l: %d; h: %d; w: %d", l(), h(), w());
    }
}
