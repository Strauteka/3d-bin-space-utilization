package org.strauteka.jbin.core;

import org.strauteka.jbin.core.Utils.Rotation;

public class Cargo<T extends Dimension> extends Space {

    private final T cargo;
    private final Dimension stack;
    private final Rotation rotate;

    public Cargo(T ref, Rotation rotate, Dimension position) {
        this(ref, 1, 1, 1, rotate, position);
    }

    public Cargo(T ref, Dimension position) {
        this(ref, 1, 1, 1, Rotation.lhw, position);
    }

    public Cargo(T ref, int l, int h, int w, Dimension position) {
        this(ref, l, h, w, Rotation.lhw, position);
    }

    public Cargo(T ref, int l, int h, int w, Rotation rotate, Dimension position) {
        this(ref, l, h, w, rotate, ref.rotate(rotate), position);
    }

    private Cargo(T ref, int l, int h, int w, Rotation rotate, Dimension rotatedRef, Dimension position) {
        super(rotatedRef.l() * l, rotatedRef.h() * h, rotatedRef.w() * w, position);
        this.stack = new Size(l, h, w);
        this.cargo = ref;
        this.rotate = rotate;
    }

    public T cargo() {
        return this.cargo;
    }

    public Dimension stack() {
        return this.stack;
    }

    public Rotation rotation() {
        return this.rotate;
    }
}
