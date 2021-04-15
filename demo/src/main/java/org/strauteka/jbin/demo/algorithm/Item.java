package org.strauteka.jbin.demo.algorithm;

import java.util.stream.Stream;

import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Rotation;

public class Item extends Size implements ItemImpl {
    private final int qty;
    private final int priority;
    public final boolean allowL;
    public final boolean allowH;
    public final boolean allowW;
    public final Rotation[] rotations;

    public Item(Dimension dimension, int qty) {
        this(dimension.l(), dimension.h(), dimension.w(), qty, true, true, true, 1);
    }

    public Item(int l, int h, int w, int qty) {
        this(l, h, w, qty, true, true, true, 1);
    }

    public Item(int l, int h, int w, int qty, boolean allowL, boolean allowH, boolean allowW, int priority) {
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
        if (o == null)
            return false;
        if (o == this)
            return true;

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * Integer.valueOf(qty).hashCode() * rotations.hashCode()
                * Boolean.valueOf(allowL).hashCode() * Boolean.valueOf(allowH).hashCode()
                * Boolean.valueOf(allowW).hashCode();
    }

    @Override
    public int priority() {
        return priority;
    }
}
