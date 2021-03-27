package org.strauteka.jbin.demo.algorithm;

import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Size;

public class Item extends Size {
    private final int qty;

    public Item(Dimension dimension, int qty) {
        this(dimension.l(), dimension.h(), dimension.w(), qty);
    }

    public Item(int l, int h, int w, int qty) {
        super(l, h, w);
        this.qty = qty;
    }

    public int qty() {
        return this.qty;
    }
}
