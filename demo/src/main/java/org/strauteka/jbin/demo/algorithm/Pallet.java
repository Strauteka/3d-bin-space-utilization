package org.strauteka.jbin.demo.algorithm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.strauteka.jbin.core.AbstractBin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Size;
import org.strauteka.jbin.core.Space;
import org.strauteka.jbin.core.Rotation;
import org.strauteka.jbin.core.configuration.StackConfig;

public class Pallet extends AbstractBin<Pallet> implements ItemImpl {
    public final Rotation[] rotations;

    private Pallet(Pallet pallet, StackConfig overStack) {
        this(pallet, Optional.ofNullable(overStack).orElseGet(() -> pallet.stackConfig()), pallet.emptySpace(),
                pallet.cargo());
    }

    private Pallet(int l, int h, int w) {
        this(new Size(l, h, w));
    }

    private Pallet(int l, int h, int w, StackConfig overStack) {
        this(new Size(l, h, w), overStack);
    }

    private Pallet(Dimension size) {
        this(size, new StackConfig(0, 0, 0, 0, 0, false));
    }

    private Pallet(Dimension size, StackConfig overStack) {
        this(size, overStack, null, null);
    }

    private Pallet(Dimension size, StackConfig overStack, List<Space> space, List<Cargo<? extends Dimension>> cargo) {
        super(size, overStack, space, cargo);
        rotations = Stream.of(Rotation.lhw, Rotation.whl).toArray(Rotation[]::new);
    }

    public static Pallet pallet(Dimension size, int h) {
        return new Pallet(size).add(new Cargo<Size>(new Size(size.l(), h, size.w()), new Size(0, 0, 0)));
    }

    public static Pallet pallet(Dimension size, int h, StackConfig overStack) {
        return new Pallet(size, overStack).add(new Cargo<Size>(new Size(size.l(), h, size.w()), new Size(0, 0, 0)));
    }

    @Override
    public Pallet add(Cargo<? extends Dimension> cargo, boolean disableTop) {
        return new Pallet(this, super.stackConfig(),
                super.dropUnusableSpace(super.mergeAll(
                        super.dropOverlapSpace(super.createSpace(super.emptySpace(), cargo, disableTop)))),
                Stream.concat(super.cargo().stream(), Stream.of(cargo)).collect(Collectors.toList()));
    }

    @Override
    public Pallet self() {
        return this;
    }

    @Override
    public int qty() {
        return 1;
    }

    @Override
    public int priority() {
        return 0;
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
        return super.hashCode();
    }

    @Override
    public Pallet binRotate(Rotation rotation) {
        return this.cargo().stream().reduce(new Pallet(rotate(rotation), this.stackConfig()),
                (bin, cargo) -> bin.add(cargo.cargoRotate(bin, rotation)), (bin1, bin2) -> {
                    throw new UnsupportedOperationException();
                });
    }
}
