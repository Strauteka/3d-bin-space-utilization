package org.strauteka.jbin.demo.algorithm;

import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Rotation;
import org.strauteka.jbin.core.Size;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class ItemImpl extends Size implements Item {
    private final int qty;
    private final int priority;
    public final boolean allowL;
    public final boolean allowH;
    public final boolean allowW;
    public final Rotation[] rotations;

    public ItemImpl(Dimension dimension, int qty) {
        this(dimension.l(), dimension.h(), dimension.w(), qty, true, true, true, 1);
    }

    public ItemImpl(int l, int h, int w, int qty) {
        this(l, h, w, qty, true, true, true, 1);
    }

    public ItemImpl(int l, int h, int w, int qty, boolean allowL, boolean allowH, boolean allowW, int priority) {
        super(l, h, w);
        this.qty = qty;
        this.allowL = allowL;
        this.allowH = allowH;
        this.allowW = allowW;
        this.priority = priority;
        rotations = Stream.of(Rotation.values())
                .filter(e -> this.allowL && (e.equals(Rotation.wlh) || e.equals(Rotation.hlw))//
                        || this.allowH && (e.equals(Rotation.whl) || e.equals(Rotation.lhw))//
                        || this.allowW && (e.equals(Rotation.lwh) || e.equals(Rotation.hwl)))
                .toArray(Rotation[]::new);
    }

    @Override
    public int qty() {
        return this.qty;
    }

    @Override
    public Rotation[] rotations() {
        return rotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemImpl)) return false;
        if (!super.equals(o)) return false;
        ItemImpl item = (ItemImpl) o;
        return qty == item.qty &&
                priority == item.priority
                && allowL == item.allowL
                && allowH == item.allowH
                && allowW == item.allowW
                && Arrays.equals(rotations, item.rotations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), qty, priority, allowL, allowH, allowW);
        result = 31 * result + Arrays.hashCode(rotations);
        return result;
    }

    @Override
    public int priority() {
        return priority;
    }
}
