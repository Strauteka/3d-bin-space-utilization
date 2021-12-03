package org.strauteka.jbin.core;

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

    public Cargo<? extends Dimension> cargoRotate(Dimension holder, Rotation rotation) {
        final Dimension newStack = this.stack().rotate(rotation);
        return new Cargo<Dimension>(this.cargo(), newStack.l(), newStack.h(), newStack.w(), createRotation(rotation),
                newPosition(holder, rotation));
    }

    private Dimension sidePos(Rotation rotation) {
        return new Size(pos(rotation.name().charAt(0)), pos(rotation.name().charAt(1)), pos(rotation.name().charAt(2)));
    }

    private Rotation createRotation(Rotation rotation) {
        final Dimension rotate = sidePos(rotation);
        return Rotation.valueOf(String.valueOf(this.rotate.name().charAt(rotate.l())) +
                this.rotate.name().charAt(rotate.h()) + this.rotate.name().charAt(rotate.w()));
    }

    private Dimension newPosition(Dimension holder, Rotation rotation) {
        final Space s = new Space(this.rotate(rotation), this.position().rotate(rotation));
        final Dimension pos = sidePos(rotation);
        final int l = pos.l() != 0 && pos.h() == 1 ? holder.l() - s.l__() : s.l_();
        final int h = pos.h() != 1 && pos.w() == 2 ? holder.h() - s.h__() : s.h_();
        final int w = pos.w() != 2 && pos.l() == 0 ? holder.w() - s.w__() : s.w_();
        return new Size(l, h, w);
    }

    private int pos(final char a) {
        switch (a) {
        case 'l':
            return 0;
        case 'h':
            return 1;
        default:
            return 2;
        }
    }
}
